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

            if (delete.IsSuccessStatusCode)
            {
                return true;
            }

            return false;
        }
    }
}