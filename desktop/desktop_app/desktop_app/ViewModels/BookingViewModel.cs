using System.Collections.ObjectModel;
using System.Windows;
using desktop_app.Models;
using desktop_app.Services;

namespace desktop_app.ViewModels
{
    public class BookingViewModel
    {
        public ObservableCollection<BookingModel> Bookings { get; }

        public BookingViewModel()
        {
            Bookings = new ObservableCollection<BookingModel>();
            _ = LoadBookingsAsync();
        }

        private async Task LoadBookingsAsync()
        {
            try
            {
                var list = await BookingService.GetAllBookingsAsync();
                Bookings.Clear();
                foreach (var booking in list)
                {
                    //booking.ClientName = await UserService.GetClientNameByIdAsync(booking.Client);
                    booking.RoomNumber = await RoomService.GetRoomNumberByIdAsync(booking.Room);
                    Bookings.Add(booking);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Ha ocurrido un error al cargar las reservas", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                Console.WriteLine($"Error cargando reservas: {ex.Message}");
            }
        }
    }

}
