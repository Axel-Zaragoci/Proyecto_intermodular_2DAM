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
    public class FormBookingViewModel : ViewModelBase
    {
        private static FormBookingViewModel? _instance;
        public static FormBookingViewModel Instance =>
            _instance ??= new FormBookingViewModel();

        public string Title =>
            string.IsNullOrEmpty(Booking.Id)
                ? "Crear reserva"
                : "Actualizar reserva";

        public bool Enabled => string.IsNullOrEmpty(Booking.Id);
        public bool Disabled => !Enabled;


        private BookingModel _booking;
        public BookingModel Booking
        {
            get => _booking;
            set
            {
                _booking = value;
                OnPropertyChanged();
                RefreshAll();
            }
        }


        private ObservableCollection<UserModel> _clients = new();
        public ObservableCollection<UserModel> Clients
        {
            get => _clients;
            set => SetProperty(ref _clients, value);
        }

        private async void LoadClients()
        {
            Clients.Clear();
            var users = await UserService.GetAllUsersAsync();
            foreach (var user in users)
                Clients.Add(user);
        }

        public string ClientDni
        {
            get => Booking.ClientDni;
            set
            {
                Booking.ClientDni = value;
                OnPropertyChanged();
            }
        }


        private ObservableCollection<RoomModel> _rooms = new();
        public ObservableCollection<RoomModel> Rooms
        {
            get => _rooms;
            set => SetProperty(ref _rooms, value);
        }

        private async void LoadRooms()
        {
            Rooms.Clear();
            var rooms = (await RoomService.GetRoomsFilteredAsync()).Items;
            foreach (var room in rooms)
                Rooms.Add(room);
        }

        public string RoomNumber
        {
            get => Booking.RoomNumber;
            set
            {
                Booking.RoomNumber = value;
                ChangeRoomData(Booking.RoomNumber);
                OnPropertyChanged();
            }
        }

        public void ChangeRoomData(String roomNumber)
        {
            PricePerNight = _rooms.First(room => room.RoomNumber == roomNumber).PricePerNight ?? 0;
            Offer = _rooms.First(room => room.RoomNumber == roomNumber).Offer ?? 0;
        }

        public DateTime CheckInDate
        {
            get => Booking.CheckInDate;
            set
            {
                Booking.CheckInDate = value;
                OnPropertyChanged();
                RecalculateTotal();
            }
        }

        public DateTime CheckOutDate
        {
            get => Booking.CheckOutDate;
            set
            {
                Booking.CheckOutDate = value;
                OnPropertyChanged();
                RecalculateTotal();
            }
        }

        public DateTime PayDate
        {
            get => Booking.PayDate;
            set
            {
                Booking.PayDate = value;
                OnPropertyChanged();
            }
        }

        public decimal PricePerNight
        {
            get => Booking.PricePerNight ?? 0;
            set
            {
                Booking.PricePerNight = value;
                OnPropertyChanged();
                RecalculateTotal();
            }
        }

        public decimal Offer
        {
            get => Booking.Offer ?? 0;
            set
            {
                Booking.Offer = value;
                OnPropertyChanged();
                RecalculateTotal();
            }
        }

        public decimal TotalPrice
        {
            get => Booking.TotalPrice;
            private set
            {
                Booking.TotalPrice = value;
                OnPropertyChanged();
            }
        }

        public int TotalNights
        {
            get => Booking.TotalNights;
            private set
            {
                Booking.TotalNights = value;
                OnPropertyChanged();
            }
        }

        public ICommand SaveCommand { get; }
        public ICommand CancelCommand { get; }

        public ICommand ReturnCommand { get; } =
            new RelayCommand(_ =>
                NavigationService.Instance.NavigateTo<BookingView>());


        private FormBookingViewModel()
        {
            Booking = new BookingModel();

            SaveCommand = new RelayCommand(async _ => await Save());
            CancelCommand = new RelayCommand(async _ => await Cancel());

            LoadClients();
            LoadRooms();
        }


        private void RecalculateTotal()
        {
            if (CheckOutDate <= CheckInDate)
            {
                TotalNights = 0;
                TotalPrice = 0;
                return;
            }

            TotalNights = (CheckOutDate.Date - CheckInDate.Date).Days;

            var basePrice = TotalNights * PricePerNight;
            var discount = basePrice * (Offer / 100m);

            TotalPrice = basePrice - discount;
        }

        private void RefreshAll()
        {
            OnPropertyChanged(nameof(Title));
            OnPropertyChanged(nameof(Enabled));
            OnPropertyChanged(nameof(Disabled));

            OnPropertyChanged(nameof(ClientDni));
            OnPropertyChanged(nameof(RoomNumber));

            OnPropertyChanged(nameof(CheckInDate));
            OnPropertyChanged(nameof(CheckOutDate));
            OnPropertyChanged(nameof(PayDate));

            OnPropertyChanged(nameof(PricePerNight));
            OnPropertyChanged(nameof(Offer));
            OnPropertyChanged(nameof(TotalPrice));
            OnPropertyChanged(nameof(TotalNights));
        }

        private async Task Save()
        {
            try
            {
                if (string.IsNullOrEmpty(Booking.Id))
                {
                    var userId = await UserService.GetUserIdByDniAsync(ClientDni);
                    Booking.Client = userId;

                    var filter = new RoomsFilter { RoomNumber = RoomNumber };
                    Booking.Room =
                        (await RoomService.GetRoomsFilteredAsync(filter))
                        ?.Items.First().Id;

                    await BookingService.CreateBookingAsync(Booking);
                }
                else
                {
                    await BookingService.UpdateBookingAsync(Booking);
                }
                await BookingEvents.RaiseBookingChanged();
                NavigationService.Instance.NavigateTo<BookingView>();
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private async Task Cancel()
        {
            if (Booking.Status == "Cancelada")
            {
                MessageBox.Show("Esta reserva ya está cancelada");
                return;
            }

            if (MessageBox.Show("¿Cancelar reserva?",
                    "Confirmación",
                    MessageBoxButton.YesNo,
                    MessageBoxImage.Warning) != MessageBoxResult.Yes)
                return;

            try
            {
                await BookingService.CancelBookingAsync(Booking.Id);
                await BookingEvents.RaiseBookingChanged();
                NavigationService.Instance.NavigateTo<BookingView>();
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }
}
