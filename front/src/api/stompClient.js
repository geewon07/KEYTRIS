import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

let stompClient = null;
let socketConnect = null;

export const connect = () => {
  const socketFactory = () => new SockJS(process.env.REACT_APP_SOCKET_BASE_URL);
  socketConnect = socketFactory(); 
  const client = Stomp.over(socketFactory);

  client.connect({}, () => {
    stompClient = client;
  }, (error) => {
    console.error("소켓 연결 실패 5초 후 재연결", error);
    setTimeout(() => connect(process.env.REACT_APP_SOCKET_BASE_URL), 5000);
  });

  return socketConnect;
};

export const disconnect = () => {
 if (stompClient && stompClient.connected) {
   stompClient.disconnect();
 }
 if (socketConnect.readyState === 1) {
    // <-- This is important
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
  //  return stompClient.subscribe(destination, message => callback(JSON.parse(message.body)));
   return stompClient.subscribe(destination, message => callback(message.body));
 }
};

export default { connect, disconnect, sendMsg , subscribe};