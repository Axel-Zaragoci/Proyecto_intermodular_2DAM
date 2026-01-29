using System.Windows;
using System.Windows.Input;
using desktop_app.Commands;
using desktop_app.Events;
using desktop_app.Models;
using desktop_app.Services;
using desktop_app.Views;

namespace desktop_app.ViewModels
{
    public class FormBookingViewModel : ViewModelBase
    {
        public string Title => Booking.Id != "" ? "Actualizar reserva" : "Crear reserva";
        
        private static FormBookingViewModel? _instance;
        public static FormBookingViewModel Instance => _instance ??= new FormBookingViewModel();
        
        public bool Enabled => _booking.Id == "";
        public bool Disabled => !Enabled;
        
        public string ClientDni
        {
            get => _booking.ClientDni;
            set
            {
                _booking.ClientDni = value;
                OnPropertyChanged();
            }
        }

        private BookingModel _booking;

        public BookingModel Booking
        {
            get => _booking;
            set
            {
                _booking = value;
                OnPropertyChanged();
                OnPropertyChanged(nameof(Title));
                OnPropertyChanged(nameof(Enabled));
                OnPropertyChanged(nameof(Disabled));
                OnPropertyChanged(nameof(ClientDni));
            }
        }

        public ICommand SaveCommand { get; }

        public ICommand ReturnCommand { get; } = new RelayCommand(_ =>
        {
            NavigationService.Instance.NavigateTo<BookingView>();
        });

        public ICommand CancelCommand { get; }
        
        private FormBookingViewModel()
        {
            Booking = new BookingModel();
            SaveCommand = new RelayCommand(async _ => await Save());
            CancelCommand = new RelayCommand(async _ => await Cancel());
        }

        private async Task Cancel()
        {
            var update = await BookingService.CancelBookingAsync(Booking.Id);
            if (update == null)
            {
                MessageBox.Show("Error al actualizar la reserva");
                return;
            }
            
            await BookingEvents.RaiseBookingChanged();
            NavigationService.Instance.NavigateTo<BookingView>();
        }
        
        private async Task Save()
        {
            if (Booking.Id == "")
            {
                var userId = await UserService.GetUserIdByDniAsync(ClientDni);
                if (userId == "Error")
                {
                    MessageBox.Show("Error al obtener el usuario");
                    return;
                }
                Booking.Client = userId;

                RoomsFilter f = new RoomsFilter();
                f.RoomNumber = Booking.RoomNumber;
                var roomId = (await RoomService.GetRoomsFilteredAsync(f))?.Items.First().Id;
                Console.WriteLine(roomId);
                if (roomId == "")
                {
                    MessageBox.Show("Error al obtener la habitación");
                    return;
                }
                Booking.Room = roomId;
                
                var create = await BookingService.CreateBookingAsync(Booking);
                if (create == null)
                {
                    MessageBox.Show("Ha ocurrido un error al crear la reserva");
                    return;
                }
            }
            else
            {
                var update = await BookingService.UpdateBookingAsync(Booking);
                if (update == null)
                {
                    MessageBox.Show("Error al actualizar la reserva");
                    return;
                }
            }
            
            await BookingEvents.RaiseBookingChanged();
            NavigationService.Instance.NavigateTo<BookingView>();
        }
    }
}