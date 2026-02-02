using desktop_app.Models;
using System;
using System.Collections.Generic;
using System.Net;         // UrlEncode
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace desktop_app.Services
{
    public static class RoomService
    {
        // FILTER  (GET /room/) : Devuelve una lista envuelta en RoomsResponse
        public static async Task<RoomsResponse?> GetRoomsFilteredAsync(RoomsFilter? f = null)
        {
            try
            {
                // Si no viene filtro, creamos uno vacío que equivale a pedir todas las habitaciones
                f ??= new RoomsFilter();

                // Preparamos los parámetros del req.query
                var parameters = new Dictionary<string, string?>();

                if (!string.IsNullOrWhiteSpace(f.Type))
                    parameters["type"] = f.Type;

   
                if (f.IsAvailable.HasValue)
                    parameters["isAvailable"] = f.IsAvailable.Value.ToString().ToLowerInvariant();

                if (f.MinPrice.HasValue)
                    parameters["minPrice"] = f.MinPrice.Value.ToString(System.Globalization.CultureInfo.InvariantCulture);

                if (f.MaxPrice.HasValue)
                    parameters["maxPrice"] = f.MaxPrice.Value.ToString(System.Globalization.CultureInfo.InvariantCulture);

                if (f.Guests.HasValue)
                    parameters["guests"] = f.Guests.Value.ToString();

                if (f.HasExtraBed.HasValue)
                    parameters["hasExtraBed"] = f.HasExtraBed.Value.ToString().ToLowerInvariant();

                if (f.HasCrib.HasValue)
                    parameters["hasCrib"] = f.HasCrib.Value.ToString().ToLowerInvariant();

                if (f.HasOffer.HasValue)
                    parameters["hasOffer"] = f.HasOffer.Value.ToString().ToLowerInvariant();

                if (f.Extras != null && f.Extras.Count > 0)
                    parameters["extras"] = string.Join(",", f.Extras);

                if (!string.IsNullOrWhiteSpace(f.SortBy))
                    parameters["sortBy"] = f.SortBy;

                if (!string.IsNullOrWhiteSpace(f.SortOrder))
                    parameters["sortOrder"] = f.SortOrder;

                // BuildQuery convierte el diccionario en texto: "?type=double&minPrice=50"
                string url = ApiService.BaseUrl + "room" + BuildQuery(parameters);

                // PETICIÓN HTTP (ASÍNCRONA)
                // 'await' espera la respuesta sin congelar la app.
                var respuesta = await ApiService._httpClient.GetAsync(url);

                if (!respuesta.IsSuccessStatusCode)
                    return new RoomsResponse(); // Si falla, devuelve vacío.

                // DESERIALIZACIÓN (JSON -> OBJETOS C#)
                string contenido = await respuesta.Content.ReadAsStringAsync();
                var opciones = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                var data = JsonSerializer.Deserialize<RoomsResponse>(contenido, opciones);

                return data ?? new RoomsResponse();
            }
            catch
            {
                return null;
            }
        }

        // recorre el diccionario y une con "&" y "=" ...
        // Usa WebUtility.UrlEncode para manejar espacios y caracteres raros.
        private static string BuildQuery(Dictionary<string, string?> parameters)
        {
            var sb = new StringBuilder();
            bool first = true;

            foreach (var kv in parameters)
            {
            
                if (string.IsNullOrWhiteSpace(kv.Value))
                    continue;

                sb.Append(first ? "?" : "&");
                first = false;

                sb.Append(WebUtility.UrlEncode(kv.Key));
                sb.Append("=");
                sb.Append(WebUtility.UrlEncode(kv.Value));
            }

            return sb.ToString();
        }

        private static readonly JsonSerializerOptions _jsonOptions =
            new JsonSerializerOptions { PropertyNameCaseInsensitive = true };

        public static async Task<RoomModel?> GetRoomByIdAsync(string roomId)
        {
            try
            {
                var url = ApiService.BaseUrl + $"room/{Uri.EscapeDataString(roomId)}";
                var resp = await ApiService._httpClient.GetAsync(url);

                if (!resp.IsSuccessStatusCode) return null;

                var json = await resp.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<RoomModel>(json, _jsonOptions);
            }
            catch
            {
                return null;
            }
        }

        // CREATE  (POST /room/)
        public static async Task<RoomModel> CreateRoomAsync(RoomModel room)
        {
            var url = ApiService.BaseUrl + "room";
            var json = JsonSerializer.Serialize(room, _jsonOptions);
            using var content = new StringContent(json, Encoding.UTF8, "application/json");

            using var resp = await ApiService._httpClient.PostAsync(url, content);
            var body = await resp.Content.ReadAsStringAsync();

            if (!resp.IsSuccessStatusCode)
                throw new HttpRequestException(
                    $"No se pudo crear la habitación. HTTP {(int)resp.StatusCode} {resp.ReasonPhrase}\n" +
                    $"Respuesta API:\n{body}\n\nPayload enviado:\n{json}"
                );

            var created = JsonSerializer.Deserialize<RoomModel>(body, _jsonOptions);

            if (created is null)
                throw new Exception(
                    $"La API respondió OK, pero no se pudo deserializar el JSON.\n" +
                    $"Body:\n{body}"
                );

            return created;
        }


        // UPDATE (PATCH /room/:roomID)
        public static async Task<bool> UpdateRoomAsync(string roomId, RoomModel room)
        {
            try
            {
                var url = ApiService.BaseUrl + $"room/{Uri.EscapeDataString(roomId)}";
                var json = JsonSerializer.Serialize(room, _jsonOptions);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var req = new HttpRequestMessage(new HttpMethod("PATCH"), url)
                {
                    Content = content
                };

                var resp = await ApiService._httpClient.SendAsync(req);
                return resp.IsSuccessStatusCode;
            }
            catch
            {
                return false;
            }
        }

        // DELETE (DELETE /room/:roomID)
        public static async Task<bool> DeleteRoomAsync(string roomId)
        {
            try
            {
                var url = ApiService.BaseUrl + $"room/{Uri.EscapeDataString(roomId)}";
                var resp = await ApiService._httpClient.DeleteAsync(url);
                return resp.IsSuccessStatusCode;
            }
            catch
            {
                return false;
            }
        }
    }
}