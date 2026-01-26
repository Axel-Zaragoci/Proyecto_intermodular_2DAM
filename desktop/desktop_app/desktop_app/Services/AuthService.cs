using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;

namespace desktop_app.Services
{
    public static class AuthService
    {
        public static async Task LoginAsync(string email, string password)
        {
            var payload = new { email, password };
            var json = JsonSerializer.Serialize(payload);
            using var content = new StringContent(json, Encoding.UTF8, "application/json");

            using var resp = await ApiService._httpClient.PostAsync("/auth/login", content);

            if (!resp.IsSuccessStatusCode) throw new Exception($"Login fallido.");

            var body = await resp.Content.ReadAsStringAsync();

            using var doc = JsonDocument.Parse(body);
            

            if (!doc.RootElement.TryGetProperty("token", out var tokenEl)) throw new Exception("Campo token vacio.");

            var token = tokenEl.GetString();

            TokenStore.AccessToken = token;
        }
    }
}
