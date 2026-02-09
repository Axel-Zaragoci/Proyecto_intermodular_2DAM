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
        /// <summary>
        /// Modo Singleton del ViewModel
        /// </summary>
        private static FormBookingViewModel? _instance;
        public static FormBookingViewModel Instance => _instance ??= new FormBookingViewModel();

        
        /// <summary>
        /// Parámetro que modifica el título de la ventana según se utilice para crear o modificar una reserva
        /// </summary>
        public string Title => Booking.Id != "" ? "Actualizar reserva" : "Crear reserva";
        
        
        /// <summary>
        /// Parámetros para activar o desactivar campos/botones según el modo de la ventana
        /// </summary>
        public bool Enabled => _booking.Id == "";
        public bool Disabled => !Enabled;
        
        
        /// <summary>
        /// Propiedad para la modificación del DNI del cliente
        /// </summary>
        public string ClientDni
        {
            get => _booking.ClientDni;
            set
            {
                _booking.ClientDni = value;
                OnPropertyChanged();
            }
        }

        private ObservableCollection<UserModel> _clients = new ObservableCollection<UserModel>();
        public ObservableCollection<UserModel> Clients
        {
            get => _clients;
            set
            {
                _clients = value;
                OnPropertyChanged();
            }
        }
        
        private async void LoadClients()
        {
            var users = await UserService.GetAllUsersAsync();

            Clients.Clear();
            foreach (var user in users)
            {
                Clients.Add(user);
            }
        }
        
        private ObservableCollection<RoomModel> _rooms = new ObservableCollection<RoomModel>();
        public ObservableCollection<RoomModel> Rooms
        {
            get => _rooms;
            set
            {
                _rooms = value;
                OnPropertyChanged();
            }
        }
        
        private async void LoadRooms()
        {
            var rooms = (await RoomService.GetRoomsFilteredAsync()).Items;

            Rooms.Clear();
            foreach (var room in rooms)
            {
                Rooms.Add(room);
            }
        }

        public String RoomNumber
        {
            get => _booking.RoomNumber;
            set
            {
                _booking.RoomNumber = value;
                OnPropertyChanged();
            }
        }
        
        /// <summary>
        /// Propiedad para la actualización de la reserva
        /// Se utiliza para determinar el modo de la ventana
        /// </summary>
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
                OnPropertyChanged(nameof(RoomNumber));
            }
        }

        
        /// <summary>
        /// Comando para el botón de guardar
        /// </summary>
        public ICommand SaveCommand { get; }

        
        /// <summary>
        /// Comando para el botón de volver
        /// </summary>
        public ICommand ReturnCommand { get; } = new RelayCommand(_ =>
        {
            NavigationService.Instance.NavigateTo<BookingView>();
        });

        
        
        /// <summary>
        /// Comando para el botón de cancelar reserva
        /// </summary>
        public ICommand CancelCommand { get; }
        
        
        /// <summary>
        /// Comando para ver los detalles de la reserva
        /// </summary>
        public ICommand ShowCommand { get; }
        
        /// <summary>
        /// Constructor de la clase
        /// Crea una reserva vacía sobre la que crear
        /// Asigna los comandos de guardar y cancelar
        /// </summary>
        private FormBookingViewModel()
        {
            Booking = new BookingModel();
            SaveCommand = new RelayCommand(async _ => await Save());
            CancelCommand = new RelayCommand(async _ => await Cancel());
            ShowCommand = new RelayCommand(_ => ShowData());
            
            LoadClients();
            LoadRooms();
        }

        
        /// <summary>
        /// Función para cancelar una reserva
        /// Se referencia en el comando de cancelar
        /// </summary>
        private async Task Cancel()
        {
            if (Booking.Status == "Cancelada")
            {
                MessageBox.Show("Esta reserva ya está cancelada", "", MessageBoxButton.OK, MessageBoxImage.Information);
                return;
            }
            
            var result = MessageBox.Show("¿Seguro que quieres cancelar esta reserva?", "Confirmar cancelación", MessageBoxButton.YesNo, MessageBoxImage.Warning);

            if (result != MessageBoxResult.Yes)
                return;
            
            try
            {
                var update = await BookingService.CancelBookingAsync(Booking.Id);
                if (update == null)
                {
                    MessageBox.Show("No se ha podido leer la respuesta del servidor");
                    return;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }
            
            await BookingEvents.RaiseBookingChanged();
            NavigationService.Instance.NavigateTo<BookingView>();
        }
        
        
        /// <summary>
        /// Función para guardar una reserva
        /// Se referencia en el comando de guardar
        /// </summary>
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
                f.RoomNumber = _booking.RoomNumber;
                var roomId = (await RoomService.GetRoomsFilteredAsync(f))?.Items.First().Id;
                
                if (roomId == "")
                {
                    MessageBox.Show("Error al obtener la habitación");
                    return;
                }
                Booking.Room = roomId;

                try
                {
                    var create = await BookingService.CreateBookingAsync(Booking);
                    if (create == null)
                    {
                        MessageBox.Show("Ha ocurrido un error al crear la reserva");
                        return;
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }
            }
            else
            {
                try
                {
                    await BookingService.UpdateBookingAsync(Booking);
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }
            }
            
            await BookingEvents.RaiseBookingChanged();
            NavigationService.Instance.NavigateTo<BookingView>();
        }

        private void ShowData()
        {
            MessageBox.Show(Booking.ToString(), "Información completa", MessageBoxButton.OK, MessageBoxImage.Information);
        }
    }
}