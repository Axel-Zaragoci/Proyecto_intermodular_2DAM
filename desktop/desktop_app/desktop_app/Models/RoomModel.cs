using desktop_app.Services;
using System.Text.Json.Serialization;

namespace desktop_app.Models
{
    public class RoomModel
    {
        // [JsonPropertyName] sirve para mapear el JSON que viene de la API (ej: "_id")
        // a tu propiedad de C# (ej: "Id"). Si no coinciden los nombres, esto es OBLIGATORIO.
        [JsonPropertyName("_id")]
        public string Id { get; set; } = "";

        [JsonPropertyName("type")]
        public string Type { get; set; } = "";

        [JsonPropertyName("roomNumber")]
        public string RoomNumber { get; set; } = "";

        [JsonPropertyName("maxGuests")]
        public int MaxGuests { get; set; }

        [JsonPropertyName("description")]
        public string Description { get; set; } = "";

        [JsonPropertyName("mainImage")]
        public string? MainImage { get; set; } // Guarda la ruta del servidor: "/uploads/foto1.jpg"

        // Esta propiedad es "inteligente". No se guarda en BD, se calcula al vuelo.
        // Sirve para que la Interfaz (Vista) tenga siempre una URL completa válida.
        public string? MainImageAbs
        {
            get
            {
                if (string.IsNullOrWhiteSpace(MainImage)) return null;
                // Si ya es absoluta (http...), la devuelve tal cual.
                if (Uri.TryCreate(MainImage, UriKind.Absolute, out _))
                    return MainImage;
                // Si es relativa (/uploads...), le pega el dominio del servidor delante.
                return ImageService.ToAbsoluteUrl(MainImage);
            }
        }

        [JsonPropertyName("pricePerNight")]
        public decimal? PricePerNight { get; set; }

        [JsonPropertyName("extraBed")]
        public bool ExtraBed { get; set; }

        [JsonPropertyName("crib")]
        public bool Crib { get; set; }

        [JsonPropertyName("offer")]
        public decimal? Offer { get; set; }

        [JsonPropertyName("extras")]
        public List<string> Extras { get; set; } = new();

        [JsonPropertyName("extraImages")]
        public List<string> ExtraImages { get; set; } = new();

        [JsonPropertyName("isAvailable")]
        public bool IsAvailable { get; set; }

        [JsonPropertyName("rate")]
        public double Rate { get; set; }
    }

        public class RoomsFilter
        {
            public string? Type { get; set; }
            public bool? IsAvailable { get; set; }
            public decimal? MinPrice { get; set; }
            public decimal? MaxPrice { get; set; }
            public int? Guests { get; set; }
            public bool? HasExtraBed { get; set; }
            public bool? HasCrib { get; set; }
            public bool? HasOffer { get; set; }
            public List<string>? Extras { get; set; }
            public string? RoomNumber { get; set; }
            public string? SortBy { get; set; }
            public string? SortOrder { get; set; }
        }
    


    public class RoomsResponse
    {
        [JsonPropertyName("items")]
        public List<RoomModel> Items { get; set; } = new();

        [JsonPropertyName("appliedFilter")]
        public System.Text.Json.JsonElement AppliedFilter { get; set; }

        [JsonPropertyName("sort")]
        public Dictionary<string, int> Sort { get; set; } = new();
    }

}