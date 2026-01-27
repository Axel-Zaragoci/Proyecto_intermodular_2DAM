using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Input;
using desktop_app.Commands;
using desktop_app.Models;
using desktop_app.Services;

namespace desktop_app.ViewModels
{
    public class BookingViewModel : ViewModelBase
    {
        public ObservableCollection<BookingModel> Bookings { get; }

        public ICommand DeleteBookingCommand { get; }
        
        public BookingViewModel()
        {
            Bookings = new ObservableCollection<BookingModel>();
            _ = LoadBookingsAsync();
            DeleteBookingCommand = new AsyncRelayCommand<BookingModel>(DeleteBookingAsync);
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
        
        private async Task DeleteBookingAsync(BookingModel booking)
        {
            var result = MessageBox.Show(
                "¿Seguro que quieres eliminar esta reserva?", "Confirmar eliminación", MessageBoxButton.YesNo, MessageBoxImage.Warning);

            if (result != MessageBoxResult.Yes)
                return;

            bool deleted = await BookingService.DeleteBooking(booking.Id);

            if (deleted)
            {
                Bookings.Remove(booking);
            }
            else
            {
                MessageBox.Show("No se pudo eliminar la reserva", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }
}
