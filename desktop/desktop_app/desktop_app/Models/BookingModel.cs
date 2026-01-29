using System.Text.Json.Serialization;
    
namespace desktop_app.Models
{
    public class BookingModel
    {
        [JsonPropertyName("_id")] 
        public string Id { get; set; } = "";
        
        [JsonPropertyName("room")]
        public string Room { get; set; }
        
        [JsonPropertyName("client")]
        public string Client { get; set; }

        [JsonPropertyName("checkInDate")]
        public DateTime CheckInDate { get; set; } = DateTime.Now;
        
        [JsonPropertyName("checkOutDate")]
        public  DateTime CheckOutDate { get; set; } = DateTime.Now;

        [JsonPropertyName("payDate")] 
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
        public int Guests { get; set; } = 0;

        [JsonPropertyName("totalNights")]
        public int TotalNights { get; set; }

        [JsonIgnore] 
        public string RoomNumber { get; set; } = "";

        [JsonIgnore] 
        public string ClientName { get; set; } = "Por conseguir";

        [JsonIgnore] 
        public string ClientDni { get; set; } = "";
        
        public BookingModel Clone()
        {
            return new BookingModel
            {
                Id = Id,
                Room = Room,
                Client = Client,
                CheckInDate = CheckInDate,
                CheckOutDate = CheckOutDate,
                PayDate = PayDate,
                TotalPrice = TotalPrice,
                PricePerNight = PricePerNight,
                Offer = Offer,
                Status = Status,
                Guests = Guests,
                TotalNights = TotalNights,
                RoomNumber = RoomNumber,
                ClientName = ClientName,
                ClientDni = ClientDni
            };
        }

    }
}
