using System.Text.Json.Serialization;

namespace desktop_app.Models
{
    public class UserModel
    {
        [JsonPropertyName("_id")]
        public string Id { get; set; }

        [JsonPropertyName("firstName")]
        public string FirstName { get; set; }

        [JsonPropertyName("lastName")]
        public string LastName { get; set; }

        [JsonPropertyName("email")]
        public string Email { get; set; }

        [JsonPropertyName("password")]
        public string Password { get; set; }

        [JsonPropertyName("dni")]
        public string Dni { get; set; }

        [JsonPropertyName("cityName")]
        public string CityName { get; set; }

        [JsonPropertyName("imageRoute")]
        public string ImageRoute { get; set; }

        [JsonPropertyName("rol")]
        public string Rol { get; set; }

        [JsonPropertyName("vipStatus")]
        public bool VipStatus { get; set; }
        
        [JsonIgnore]
        public string FullNameWithDni => $"{FirstName} {LastName} - {Dni}";
    }
}