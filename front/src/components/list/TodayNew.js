import React, { useState, useEffect } from "react";
import "./Score.css";
import { getNews } from "../../api/singleGame/SingleGameApi.js"

function TodayNew({ lastWord }) {
  // const [lastWord] = useState("인공지능");
  const [newsItems, setNewsItems] = useState([]);

  const newsReqeustDto = {
    lastWord : lastWord
  };

  const getNewsList = async() => {
    try {
      const res = await getNews(newsReqeustDto);
      let newsResponse = JSON.parse(res.data.data.NewsResponse);
      let items = newsResponse.items;
      
      for (const item in items) {
        items[item].title = items[item].title.replace(/<(\/b|b)([^>]*)>/gi, "");
        items[item].description = items[item].description.replace(/<(\/b|b)([^>]*)>/gi, "");
      }
      
      setNewsItems(items);

    } catch (error) {
      console.error(error);
    }
  }

  useEffect(() => {
    getNewsList();
    // eslint-disable-next-line
  }, []);

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
          <React.Fragment key={index}>
            <div className="news-title">
              <a href={item.link} target="_blank" rel="noopener noreferrer">
                {item.title}
              </a>
            </div>
            <div className="news-description">
              <a href={item.link} target="_blank" rel="noopener noreferrer">
                {item.description}
              </a>
            </div>
          </React.Fragment>
        ))}
      </div>
    </>
  );
}

export default TodayNew;
