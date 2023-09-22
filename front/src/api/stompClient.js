import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

let stompClient = null;
let socketConnect = null;

export const connect = () => new Promise((resolve, reject) => {
  const socketFactory = () => new SockJS(process.env.REACT_APP_SOCKET_BASE_URL);
  socketConnect = socketFactory(); 
  const client = Stomp.over(socketFactory);


  client.connect({}, () => {
    stompClient = client;
    resolve();
  }, (error) => {
    console.error("소켓 연결 실패 5초 후 재연결", error);
    setTimeout(() => connect(process.env.REACT_APP_SOCKET_BASE_URL), 5000);
    reject(error); 
  });
});

export const disconnect = () => {
  if (stompClient && stompClient.connected) {
    stompClient.disconnect();
  }
  if (socketConnect && socketConnect.readyState === WebSocket.OPEN) { 
    // WebSocket.OPEN으로 상태 확인
    socketConnect.close();
  }
 };
 
 export const sendMsg = (destination, body={}) => { 
  if(stompClient && stompClient.connected) { 
    stompClient.send(destination, {}, JSON.stringify(body));
  }
 };

export const subscribe = (destination, callback) => {
  if (stompClient && stompClient.connected) {
    return stompClient.subscribe(destination, message => {
      let parsedBody;
      try {
        parsedBody = JSON.parse(message.body);
      } catch (error) {
        parsedBody = message.body;
      }
      callback(parsedBody);
    });
  }
};

export default { connect, disconnect, sendMsg , subscribe};