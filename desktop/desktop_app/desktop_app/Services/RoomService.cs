using desktop_app.Models;
using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Net.Http.Json; // UrlEncode
using System.Text;
using System.Text.Json;

namespace desktop_app.Services
{
    public static class RoomService
    {
        //  Este método llama a GET /room con filtros por querystring
        public static async Task<RoomsResponse?> GetRoomsFilteredAsync(RoomsFilter? f = null)
        {
            try
            {
                // 1) Si no viene filtro, creamos uno vacío (equivale a "dame todo")
                f ??= new RoomsFilter();

                // 2) Preparamos los parámetros EXACTOS que tu backend espera (req.query)
                var parameters = new Dictionary<string, string?>();

                // ---- type ----
                if (!string.IsNullOrWhiteSpace(f.Type))
                    parameters["type"] = f.Type;

                // ---- isAvailable ----
                if (f.IsAvailable.HasValue)
                    parameters["isAvailable"] = f.IsAvailable.Value.ToString().ToLowerInvariant();

                // ---- minPrice / maxPrice ----
                if (f.MinPrice.HasValue)
                    parameters["minPrice"] = f.MinPrice.Value.ToString(System.Globalization.CultureInfo.InvariantCulture);

                if (f.MaxPrice.HasValue)
                    parameters["maxPrice"] = f.MaxPrice.Value.ToString(System.Globalization.CultureInfo.InvariantCulture);

                // ---- guests (tu backend lo usa para filtrar maxGuests >= guests) ----
                if (f.Guests.HasValue)
                    parameters["guests"] = f.Guests.Value.ToString();

                // ---- flags: hasExtraBed / hasCrib / hasOffer ----
                if (f.HasExtraBed.HasValue)
                    parameters["hasExtraBed"] = f.HasExtraBed.Value.ToString().ToLowerInvariant();

                if (f.HasCrib.HasValue)
                    parameters["hasCrib"] = f.HasCrib.Value.ToString().ToLowerInvariant();

                if (f.HasOffer.HasValue)
                    parameters["hasOffer"] = f.HasOffer.Value.ToString().ToLowerInvariant();

                if (!string.IsNullOrWhiteSpace(f.RoomNumber))
                    parameters["roomNumber"] = f.RoomNumber;
                
                // ---- extras: "wifi,parking" ----
                if (f.Extras != null && f.Extras.Count > 0)
                    parameters["extras"] = string.Join(",", f.Extras);

                // ---- sortBy / sortOrder ----
                if (!string.IsNullOrWhiteSpace(f.SortBy))
                    parameters["sortBy"] = f.SortBy;

                if (!string.IsNullOrWhiteSpace(f.SortOrder))
                    parameters["sortOrder"] = f.SortOrder;

                // 3) Construimos la URL final: http://localhost:3000/room?...
                string url = ApiService.BaseUrl + "room" + BuildQuery(parameters);

                // 4) Hacemos la petición GET
                var respuesta = await ApiService._httpClient.GetAsync(url);

                // 5) Si la API responde error, devolvemos un objeto vacío
                if (!respuesta.IsSuccessStatusCode)
                    return new RoomsResponse();

                // 6) Leemos el JSON que devuelve la API
                string contenido = await respuesta.Content.ReadAsStringAsync();
                
                // 7) Deserializamos a RoomsResponse (IMPORTANTE: NO ES UNA LISTA)
                var opciones = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };

                var data = JsonSerializer.Deserialize<RoomsResponse>(contenido, opciones);

                // 8) Si viniera null por cualquier cosa, devolvemos vacío
                return data ?? new RoomsResponse();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                // Si hay excepción (API apagada, red, JSON inválido...), devolvemos null
                return null;
            }
        }
        
        // Convierte diccionario => "?a=1&b=2"
        private static string BuildQuery(Dictionary<string, string?> parameters)
        {
            var sb = new StringBuilder();
            bool first = true;

            foreach (var kv in parameters)
            {
                // Si el valor no existe, no lo mandamos (así no ensuciamos la URL)
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

        public static async Task<string> GetRoomNumberByIdAsync(string id)
        {
            string url = $"{ApiService.BaseUrl}room/{id}";

            var respuesta = await ApiService._httpClient.GetAsync(url);

            if (!respuesta.IsSuccessStatusCode)
            {
                Console.WriteLine(respuesta.Content.ReadAsStringAsync().Result);
                return "Error";
            }

            RoomModel? contenido = await respuesta.Content.ReadFromJsonAsync<RoomModel>();

            return contenido?.RoomNumber ?? "Error";
        }
    }
}
