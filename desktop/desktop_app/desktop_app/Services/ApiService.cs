using System;
using System.Net.Http;

namespace desktop_app.Services
{
    public static class ApiService
    {
        public const string BaseUrl = "http://localhost:3000/";
        public static readonly HttpClient _httpClient = CreateClient();

        private static HttpClient CreateClient()
        {
            var handler = new AuthHeaderHandler
            {
                InnerHandler = new HttpClientHandler()
            };

            var client = new HttpClient(handler)
            {
                BaseAddress = new Uri(BaseUrl)
            };
            return client;
        }
    }
}
