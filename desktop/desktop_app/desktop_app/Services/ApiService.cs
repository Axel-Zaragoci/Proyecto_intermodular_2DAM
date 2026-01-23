using System.Net.Http;
using System.Net.Http.Headers;
using System.Text.Json;
using desktop_app.Models;

namespace desktop_app.Services
{
    //Clase baseica para conexión con la base de datos
    public static class ApiService
    {
        // Creada conexión http y url base para conexiones
        public static readonly HttpClient _httpClient = new HttpClient();
        public const string BaseUrl = "http://localhost:3000/";

    }
}
