using System.Text.Json.Serialization;
using desktop_app.Converters;
    
namespace desktop_app.Models
{
    public class BookingModel
    {
        [JsonPropertyName("_id")] 
        public string Id { get; set; }
        
        [JsonPropertyName("room")]
        public string Room { get; set; }
        
        [JsonPropertyName("client")]
        public string Client { get; set; }
        
        [JsonPropertyName("checkInDate")]
        [JsonConverter(typeof(CustomDateTimeConverter))]
        public DateTime CheckInDate { get; set; }
        
        [JsonPropertyName("checkOutDate")]
        [JsonConverter(typeof(CustomDateTimeConverter))]
        public  DateTime CheckOutDate { get; set; }

        [JsonPropertyName("payDate")] 
        [JsonConverter(typeof(CustomDateTimeConverter))]
        public DateTime PayDate { get; set; }
        
        [JsonPropertyName("totalPrice")]
        public decimal TotalPrice { get; set; }

        [JsonPropertyName("pricePerNight")]
        public decimal PricePerNight { get; set; }

        [JsonPropertyName("offer")]
        public decimal Offer { get; set; }

        [JsonPropertyName("status")]
        public string Status { get; set; }

        [JsonPropertyName("guests")]
        public int Guests { get; set; }

        [JsonPropertyName("totalNights")]
        public int TotalNights { get; set; }

        [JsonIgnore]
        public string RoomNumber { get; set; }

        [JsonIgnore] 
        public string ClientName { get; set; } = "Por conseguir";
    }
}
