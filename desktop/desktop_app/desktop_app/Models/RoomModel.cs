using System.Text.Json.Serialization;

namespace desktop_app.Models
{
    public class RoomModel
    {
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
        public string MainImage { get; set; } = "";

        [JsonPropertyName("pricePerNight")]
        public decimal PricePerNight { get; set; }

        [JsonPropertyName("extraBed")]
        public bool ExtraBed { get; set; }

        [JsonPropertyName("crib")]
        public bool Crib { get; set; }

        [JsonPropertyName("offer")]
        public int Offer { get; set; }

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
            public string? SortBy { get; set; }
            public string? SortOrder { get; set; }
        }
    


    public class RoomsResponse
    {
        [JsonPropertyName("items")]
        public List<RoomModel> Items { get; set; } = new();

        // Si no lo usas, puedes dejarlo como JsonElement para no modelarlo entero
        [JsonPropertyName("appliedFilter")]
        public System.Text.Json.JsonElement AppliedFilter { get; set; }

        [JsonPropertyName("sort")]
        public Dictionary<string, int> Sort { get; set; } = new();
    }
}
