using System.Net.Http;
using System.Net.Http.Json;
using desktop_app.Models;
using Newtonsoft.Json;

namespace desktop_app.Services
{
    public static class BookingService
    {
        public static async Task<List<BookingModel>> GetAllBookingsAsync()
        {
            return (await (await CreateResponse("", new Object(), HttpMethod.Get)).Content.ReadFromJsonAsync<List<BookingModel>>()) ?? new List<BookingModel>();
        }

        public static async Task<bool> DeleteBooking(string bookingId)
        {
            return (await CreateResponse(bookingId, new Object(), HttpMethod.Delete)).IsSuccessStatusCode;
        }

        public static async Task<BookingModel?> UpdateBookingAsync(BookingModel booking)
        {
            var payload = new {
                checkInDate = DateTime.SpecifyKind(booking.CheckInDate, DateTimeKind.Utc),
                checkOutDate = DateTime.SpecifyKind(booking.CheckOutDate, DateTimeKind.Utc),
                guests = booking.Guests};

            var response = await CreateResponse(booking.Id, payload, HttpMethod.Patch);
            
            var updatedBooking = await response.Content.ReadFromJsonAsync<BookingModel>();

            if (updatedBooking != null)
            {
                updatedBooking.CheckInDate = DateTime.SpecifyKind(updatedBooking.CheckInDate, DateTimeKind.Utc).ToLocalTime();
                updatedBooking.CheckOutDate = DateTime.SpecifyKind(updatedBooking.CheckOutDate, DateTimeKind.Utc).ToLocalTime();
                updatedBooking.PayDate = DateTime.SpecifyKind(updatedBooking.PayDate, DateTimeKind.Utc).ToLocalTime();
            }
            
            return updatedBooking;
        }

        public static async Task<BookingModel?> CreateBookingAsync(BookingModel booking)
        {
            var payload = new {client = booking.Client, room = booking.Room, checkInDate = booking.CheckInDate, checkOutDate = booking.CheckOutDate, guests = booking.Guests};

            var response = await CreateResponse("", payload, HttpMethod.Post);
            
            var updatedBooking = await response.Content.ReadFromJsonAsync<BookingModel>();

            return updatedBooking;
        }

        public static async Task<BookingModel?> CancelBookingAsync(string bookingId)
        {
            var response = await CreateResponse($"{bookingId}/cancel", new Object(), HttpMethod.Patch);
            
            var updatedBooking = await response.Content.ReadFromJsonAsync<BookingModel>();

            return updatedBooking;
        }

        private static async Task<HttpResponseMessage> CreateResponse(string endpoint, object payload, HttpMethod method)
        {
            string url = $"{ApiService.BaseUrl}booking/{endpoint}";
            
            var request = new HttpRequestMessage(method, url)
            {
                Content = JsonContent.Create(payload)
            };

            var response = await ApiService._httpClient.SendAsync(request);

            await HandleError(response);
            
            return response;
        }

        private static Task HandleError (HttpResponseMessage response)
        {
            if (!response.IsSuccessStatusCode)
            {
                string error = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine("Error en la API de booking: " + error);
                var value = JsonConvert.DeserializeObject<Dictionary<string, string>>(error);
                if (value != null)
                {
                    var errors = value["error"];
                    throw new Exception(errors);
                }
                string errString = String.Join("\n", error.Split(", "));
                throw new Exception(errString);
            }
            return Task.CompletedTask;
        }
    }
}