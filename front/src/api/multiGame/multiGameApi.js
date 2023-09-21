import axiosInstance from "../index";


export const createMultiRoom = async (MultiGameCreateRequest) => {
    const response = await axiosInstance.post("/multigames", MultiGameCreateRequest);
    return response;
  };

export const connectMultiRoom = async (roomId, MultiGameConnectRequest) => {
  const response = await axiosInstance.post(`/multigames/${roomId}`,MultiGameConnectRequest);
  return response;
};