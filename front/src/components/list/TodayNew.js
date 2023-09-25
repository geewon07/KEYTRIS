import React, { useState, useEffect } from "react";
import "./Score.css";
import axios from "axios";

function TodayNew() {
   // eslint-disable-next-line
  const [lastWord, setLastWord] = useState("인공지능");
  const [newList, setNewList] = useState([]);

  const getNews = async () => {
    var client_id = process.env.REACT_APP_API_NAVER_CLIENT_ID;
    var client_secret = process.env.REACT_APP_API_NAVER_CLIENT_SECRET;
    var request_body = {
      query: lastWord,
      display: 3,
      start: 1,
      sort: "date",
    };

    try {
      const response = await axios.get("/naver/v1/search/news.json", {
        params: request_body,
        headers: {
          "X-Naver-Client-Id": client_id,
          "X-Naver-Client-Secret": client_secret,
          "Content-Type": "application/json",
        },
      });
      console.log(response);
      console.log(response.data.items);

      // Replace &quot; with "
      const itemsWithQuotesReplaced = response.data.items.map((item) => ({
        ...item,
        title: item.title.replace(/&quot;/g, '"'),
        description: item.description.replace(/&quot;/g, '"'),
      }));

      setNewList(itemsWithQuotesReplaced);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    getNews();
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

        {newList.map((item, index) => (
          <React.Fragment key={index}>
            <div className="news-title">
              <a href={item.link} target="_blank" rel="noopener noreferrer">
                {item.title.replace(/<b>/gi, "").replace(/<\/b>/gi, "")}
              </a>
            </div>
            <div className="news-description">
              <a href={item.link} target="_blank" rel="noopener noreferrer">
                {item.description.replace(/<b>/gi, "").replace(/<\/b>/gi, "")}
              </a>
            </div>
          </React.Fragment>
        ))}
      </div>
    </>
  );
}

export default TodayNew;
