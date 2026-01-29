using System.Net.Http;
using System.Net.Http.Json;
using desktop_app.Models;

namespace desktop_app.Services
{
    public static class BookingService
    {
        public static async Task<List<BookingModel>> GetAllBookingsAsync()
        {
            string url = $"{ApiService.BaseUrl}booking/";

            var bookings = await ApiService._httpClient.GetFromJsonAsync<List<BookingModel>>(url);

            return bookings ?? new List<BookingModel>();
        }

        public static async Task<bool> DeleteBooking(string bookingId)
        {
            string url = $"{ApiService.BaseUrl}booking/{bookingId}";
            
            var delete = await ApiService._httpClient.DeleteAsync(url);

            return delete.IsSuccessStatusCode;
        }

        public static async Task<BookingModel?> UpdateBookingAsync(BookingModel booking)
        {
            string url = $"{ApiService.BaseUrl}booking/{booking.Id}";

            var payload = new {
                checkInDate = DateTime.SpecifyKind(booking.CheckInDate, DateTimeKind.Utc),
                checkOutDate = DateTime.SpecifyKind(booking.CheckOutDate, DateTimeKind.Utc),
                guests = booking.Guests};

            var request = new HttpRequestMessage(HttpMethod.Patch, url)
            {
                Content = JsonContent.Create(payload)
            };

            var response = await ApiService._httpClient.SendAsync(request);

            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine(response.Content.ReadAsStringAsync().Result);
                return null;
            };
            
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
            string url = $"{ApiService.BaseUrl}booking/";

            var payload = new {client = booking.Client, room = booking.Room, checkInDate = booking.CheckInDate, checkOutDate = booking.CheckOutDate, guests = booking.Guests};

            var request = new HttpRequestMessage(HttpMethod.Post, url)
            {
                Content = JsonContent.Create(payload)
            };

            var response = await ApiService._httpClient.SendAsync(request);

            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine(response.Content.ReadAsStringAsync().Result);
                return null;
            };
            
            var updatedBooking = await response.Content.ReadFromJsonAsync<BookingModel>();

            return updatedBooking;
        }

        public static async Task<BookingModel?> CancelBookingAsync(string bookingId)
        {
            string url = $"{ApiService.BaseUrl}booking/{bookingId}/cancel";

            var request = new HttpRequestMessage(HttpMethod.Patch, url);

            var response = await ApiService._httpClient.SendAsync(request);
            
            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine(response.Content.ReadAsStringAsync().Result);
                return null;
            };
            
            var updatedBooking = await response.Content.ReadFromJsonAsync<BookingModel>();

            return updatedBooking;
        }
    }
}