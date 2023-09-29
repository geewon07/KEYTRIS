import React, { useState, useEffect } from "react";
import "./Score.css";
import { getNews } from "../../api/singleGame/SingleGameApi.js"

function TodayNew({ lastWord }) {
  // const [lastWord] = useState("인공지능");
  const [newsItems, setNewsItems] = useState([]);

  const newsReqeustDto = {
    lastWord: lastWord
  };

  const getNewsList = async () => {
    try {
      const res = await getNews(newsReqeustDto);
      let newsResponse = JSON.parse(res.data.data.NewsResponse);
      let items = newsResponse.items;

      for (const item in items) {
        items[item].title = items[item].title.replace(/<(\/)?([a-zA-Z]*)(\s[a-zA-Z]*=[^>]*)?(\s)*(\/)?>/ig, "").replace(/&quot;/g, "\"");
        items[item].description = items[item].description.replace(/<(\/)?([a-zA-Z]*)(\s[a-zA-Z]*=[^>]*)?(\s)*(\/)?>/ig, "").replace(/&quot;/g, "\"");
      }

      setNewsItems(items);

    } catch (error) {
      console.error(error);
    }
  }

  useEffect(() => {
    getNewsList();
    // eslint-disable-next-line
  }, [lastWord]);

  return (
    <>
      <div className="news">
        <div className="list-title">오늘의 기사</div>
        <hr />
        <div className="today-keyword">
          오늘 나의 키워드는 <span className="highlight">"{lastWord}"</span>
          &nbsp;입니다.
        </div>

        {newsItems.map((item, index) => (
          <React.Fragment key={index} >
            <div className="my-news-container1" >
              <div className="news-title">
                <a href={item.link} target="_blank" rel="noopener noreferrer">
                  {item.title}
                </a>
              </div>
              <div className="news-description">
                <a href={item.link} target="_blank" rel="noopener noreferrer" className="my-news-content">
                  {item.description}
                </a>
              </div>
            </div>
          </React.Fragment>
        ))}
      </div>
    </>
  );
}

export default TodayNew;
