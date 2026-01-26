using System.Net.Http;
using System.Net.Http.Headers;

namespace desktop_app.Services
{
    public class AuthHeaderHandler : DelegatingHandler
    {
        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken ct)
        {
            var token = TokenStore.AccessToken;

            if (!string.IsNullOrWhiteSpace(token))
            {
                request.Headers.Authorization = new AuthenticationHeaderValue("Bearer", token);
            }

            return base.SendAsync(request, ct);
        }
    }
}