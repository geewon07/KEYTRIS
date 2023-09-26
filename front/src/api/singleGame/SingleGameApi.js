import axiosInstance from "../index";


export const createRoom = async (roomCreateDto) => {
    const response = await axiosInstance.post("/games", roomCreateDto);
    return response;
  };

export const startGame = async (statusRequestDto) => {
  const response = await axiosInstance.post("/games/start",statusRequestDto);
  return response;
};


export const insertWord = async (insertRequestDto) => {
  const response = await axiosInstance.post("/games/guess-word",insertRequestDto);
  return response;
};

export const overGame = async (overRequestDto) => {
  const response = await axiosInstance.post("/games/over",overRequestDto);
  return response
}

// export const outRoom = async (statusRequestDto) => {
//   const response = await axiosInstance.post("/games/out",statusRequestDto);
//   return response
// }

export const rankPlayer = async (rankingRequestDto) => {
  const response = await axiosInstance.post("/ranking",rankingRequestDto);
  return response
}

export const getNews = async (getNewsRequestDto) => {
  const response = await axiosInstance.post("games/news",getNewsRequestDto);
  return response
}
