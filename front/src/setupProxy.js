const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    createProxyMiddleware("/naver", {
      target: "https://openapi.naver.com", // --> 이 긴 주소를 api로 바꿔 사용한다는 약속
      changeOrigin: true,
      pathRewrite: {
        "^/naver/": "/",
      },
    })
  );
};
