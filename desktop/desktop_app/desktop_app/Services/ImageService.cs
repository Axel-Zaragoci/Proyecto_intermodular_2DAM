using desktop_app.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace desktop_app.Services
{
    public static class ImageService
    {
        private static readonly JsonSerializerOptions _jsonOptions =
            new JsonSerializerOptions { PropertyNameCaseInsensitive = true };

        // POST /image (1 archivo, key "photo")
        public static async Task<UploadFileDto?> UploadSingleAsync(string filePath)
        {
            try
            {
                using var form = new MultipartFormDataContent();

                var bytes = await File.ReadAllBytesAsync(filePath);
                var fileContent = new ByteArrayContent(bytes);
                fileContent.Headers.ContentType = new MediaTypeHeaderValue(GetMimeType(filePath));

                form.Add(fileContent, "photo", Path.GetFileName(filePath));

                var url = ApiService.BaseUrl + "image";
                var resp = await ApiService._httpClient.PostAsync(url, form);
                if (!resp.IsSuccessStatusCode) return null;

                var json = await resp.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<UploadFileDto>(json, _jsonOptions);
            }
            catch
            {
                return null;
            }
        }

        // POST /image/many (varios archivos, key "photos")
        public static async Task<List<UploadFileDto>?> UploadManyAsync(IEnumerable<string> filePaths)
        {
            try
            {
                using var form = new MultipartFormDataContent();

                foreach (var path in filePaths)
                {
                    var bytes = await File.ReadAllBytesAsync(path);
                    var fileContent = new ByteArrayContent(bytes);
                    fileContent.Headers.ContentType = new MediaTypeHeaderValue(GetMimeType(path));

                
                    form.Add(fileContent, "photos", System.IO.Path.GetFileName(path));
                }

                var url = ApiService.BaseUrl + "image/many";
                var resp = await ApiService._httpClient.PostAsync(url, form);
                if (!resp.IsSuccessStatusCode) return null;

                var json = await resp.Content.ReadAsStringAsync();
                var data = JsonSerializer.Deserialize<UploadManyResponse>(json, _jsonOptions);

                return data?.Files ?? new List<UploadFileDto>();
            }
            catch
            {
                return null;
            }
        }

        // Convierte "/uploads/xxx.jpg" en URL absoluta para usar en WPF
        public static string ToAbsoluteUrl(string relativeUrl)
        {
            var baseUrl = ApiService.BaseUrl.TrimEnd('/');      // "http://localhost:3000"
            var rel = relativeUrl.StartsWith("/") ? relativeUrl : "/" + relativeUrl;
            return baseUrl + rel;
        }

        private static string GetMimeType(string path)
        {
            var ext = System.IO.Path.GetExtension(path).ToLowerInvariant();
            return ext switch
            {
                ".jpg" or ".jpeg" => "image/jpeg",
                ".png" => "image/png",
                ".webp" => "image/webp",
                ".gif" => "image/gif",
                _ => "application/octet-stream"
            };
        }
        public static async Task<bool> DeleteImageAsync(string urlOrFilename)
        {
            try
            {
                if (string.IsNullOrWhiteSpace(urlOrFilename))
                    return false;

                // Acepta:
                //  - "/uploads/abc.jpg"
                //  - "abc.jpg"
                //  - "http://localhost:3000/uploads/abc.jpg"
                var filename = ExtractFilename(urlOrFilename);
                if (string.IsNullOrWhiteSpace(filename))
                    return false;

                var url = ApiService.BaseUrl.TrimEnd('/') + "/image/" + Uri.EscapeDataString(filename);
                var resp = await ApiService._httpClient.DeleteAsync(url);

                return resp.IsSuccessStatusCode;
            }
            catch
            {
                return false;
            }
        }

        private static string ExtractFilename(string urlOrFilename)
        {
            // Si viene como URL completa
            if (Uri.TryCreate(urlOrFilename, UriKind.Absolute, out var abs))
            {
                // abs.AbsolutePath = "/uploads/abc.jpg"
                var path = abs.AbsolutePath;
                return System.IO.Path.GetFileName(path);
            }

            // Si viene como "/uploads/abc.jpg" o "uploads/abc.jpg"
            if (urlOrFilename.Contains("/"))
                return System.IO.Path.GetFileName(urlOrFilename);

            // Si viene como "abc.jpg"
            return urlOrFilename.Trim();
        }


    }
}

