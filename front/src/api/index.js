import Axios from "axios";

const axiosInstance = Axios.create({
  baseURL: process.env.REACT_APP_API_BASE_URL
  // baseURL: "http://localhost:8765/api"
});

export default axiosInstance;