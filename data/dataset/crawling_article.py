import requests
from bs4 import BeautifulSoup
from datetime import datetime
from tqdm import tqdm
import json
from konlpy.tag import Okt
import re
# import hdfs

sids = [i for i in range(100, 106)]
for sid in sids:
  def ex_tag(sid, page):
    ### 뉴스 분야(sid)와 페이지(page)를 입력하면 그에 대한 링크들을 리스트로 추출하는 함수 ###
    url = f"https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1={sid}" \
          "#&date=%2000:00:00&page={page}"
    html = requests.get(url, headers={"User-Agent": "Mozilla/5.0" \
                                                    "(Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " \
                                                    "Chrome/110.0.0.0 Safari/537.36"})
    soup = BeautifulSoup(html.text, "lxml")
    a_tag = soup.find_all("a")

    tag_lst = []
    for a in a_tag:
      if "href" in a.attrs:
        if (f"sid={sid}" in a["href"]) and ("article" in a["href"]):
          tag_lst.append(a["href"])

    return tag_lst


  def re_tag(sid):
    ### 특정 분야의 100페이지까지의 뉴스의 링크를 수집하여 중복 제거한 리스트로 변환하는 함수 ###
    re_lst = []
    for i in tqdm(range(100)):
      lst = ex_tag(sid, i + 1)
      re_lst.extend(lst)

    # 중복 제거
    re_set = set(re_lst)
    re_lst = list(re_set)

    return re_lst


  all_hrefs = {}

  # 각 분야별로 링크 수집해서 딕셔너리에 저장

  sid_data = re_tag(sid)
  all_hrefs[sid] = sid_data


  def art_crawl(all_hrefs, sid, index):

    # sid(int): 분야[100: 정치, 101: 경제, 102: 사회, 103: 생활 / 문화, 104: 세계, 105: IT / 과학]

    art_dic = {}

    title_selector = "#title_area > span"
    date_selector = "#ct > div.media_end_head.go_trans > div.media_end_head_info.nv_notrans" \
                    "> div.media_end_head_info_datestamp > div:nth-child(1) > span"
    main_selector = "#dic_area"

    url = all_hrefs[sid][index]
    html = requests.get(url, headers={"User-Agent": "Mozilla/5.0 " \
                                                    "(Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" \
                                                    "Chrome/110.0.0.0 Safari/537.36"})
    soup = BeautifulSoup(html.text, "lxml")

    # 제목 수집
    title = soup.select(title_selector)
    title_lst = [t.text for t in title]
    title_str = "".join(title_lst)

    # 날짜 수집
    date = soup.select(date_selector)
    date_lst = [d.text[:10] for d in date]
    date_str = "".join(date_lst)

    # 현재 날짜 가져오기
    today = datetime.now().strftime('%Y.%m.%d')

    # 오늘 날짜인 경우만 처리
    if date_str == today:
      # 본문 수집
      main = soup.select(main_selector)
      main_lst = []
      for m in main:
        m_text = m.text
        m_text = m_text.strip()
        main_lst.append(m_text)
      main_str = "".join(main_lst)

      art_dic["title"] = title_str
      art_dic["date"] = date_str
      art_dic["main"] = main_str

      return art_dic

  # 모든 섹션의 데이터 수집 (제목, 날짜, 본문, section, url)
  section_lst = [s for s in range(sid, sid + 1)]
  artdic_lst = []

  for section in section_lst:
    for i in range(len(all_hrefs[section])):
      art_dic = art_crawl(all_hrefs, section, i)
      if art_dic:
        art_dic["section"] = section
        art_dic["url"] = all_hrefs[section][i]
        artdic_lst.append(art_dic)

  # # HDFS 클라이언트 생성
  # client = hdfs.InsecureClient('http://localhost:9000', user='dev')

  # # HDFS 루트 경로 확인
  # root_path = client.status('/').get('pathSuffix')

  # 현재 날짜를 얻어 파일 이름 생성
  today_date = datetime.now().strftime('%Y-%m-%d')

  # # JSON 데이터를 HDFS에 직접 저장
  # # hdfs_path = f'/{root_path}/article_data_{today_date}_{sid}.json'
  # hdfs_path = f'/article/article_data_{today_date}_{sid}.json'
  # json_data = json.dumps(artdic_lst, ensure_ascii=False, indent=4)
  # with client.write(hdfs_path, encoding='utf-8') as writer:
  #   writer.write(json_data)

  # 데이터프레임을 JSON 형식으로 변환
  json_data = json.dumps(artdic_lst, ensure_ascii=False, indent=4)

  # 현재 날짜를 얻어 파일 이름 생성
  txt_file_path = f"article_data_100p_{today_date}_{sid}.json"

  # JSON 데이터를 txt 파일로 저장
  with open(txt_file_path, 'w', encoding='utf-8') as json_file:
    json_file.write(json_data)

# 다른 Python 스크립트 실행
with open('preprocessing_article.py', 'r', encoding='utf-8') as file:
  code = file.read()
  exec(code)
