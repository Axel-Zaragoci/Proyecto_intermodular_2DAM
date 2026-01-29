using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Input;
using desktop_app.Commands;
using desktop_app.Events;
using desktop_app.Models;
using desktop_app.Services;
using desktop_app.Views;

namespace desktop_app.ViewModels
{
    public class BookingViewModel : ViewModelBase
    {
        public ObservableCollection<BookingModel> Bookings { get; }
        public ICommand DeleteBookingCommand { get; }
        public ICommand EditBookingCommand { get; }
        public ICommand CreateBookingCommand { get; }
        
        public BookingViewModel()
        {
            Bookings = new ObservableCollection<BookingModel>();
            _ = LoadBookingsAsync();
            DeleteBookingCommand = new AsyncRelayCommand<BookingModel>(DeleteBookingAsync);
            EditBookingCommand = new RelayCommand(EditBooking);
            CreateBookingCommand = new RelayCommand(CreateBooking);

            BookingEvents.OnBookingChanged += async () => await LoadBookingsAsync();
        }

        private async Task LoadBookingsAsync()
        {
            try
            {
                var list = await BookingService.GetAllBookingsAsync();
                Bookings.Clear();
                foreach (var booking in list)
                {
                    booking.ClientName = await UserService.GetClientNameByIdAsync(booking.Client);
                    booking.ClientDni = await UserService.GetUserDniByIdAsync(booking.Client);
                    booking.RoomNumber = await RoomService.GetRoomNumberByIdAsync(booking.Room);
                    Bookings.Add(booking);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
        
        private async Task DeleteBookingAsync(BookingModel booking)
        {
            var result = MessageBox.Show(
                "¿Seguro que quieres eliminar esta reserva?", "Confirmar eliminación", MessageBoxButton.YesNo, MessageBoxImage.Warning);

            if (result != MessageBoxResult.Yes)
                return;

            try
            {
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
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private async void EditBooking(object? parameter)
        {
            if (parameter is not BookingModel booking) return;
            NavigationService.Instance.NavigateTo<FormBookingView>();
            FormBookingViewModel.Instance.Booking = booking;
            string dni = await UserService.GetUserDniByIdAsync(booking.Client);
            FormBookingViewModel.Instance.ClientDni = dni;
        }

        private void CreateBooking(object? parameter)
        {
            NavigationService.Instance.NavigateTo<FormBookingView>();
            FormBookingViewModel.Instance.Booking = new BookingModel();
        }
    }
}
