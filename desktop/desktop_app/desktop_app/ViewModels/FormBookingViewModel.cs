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
        /// Parámetro que modifica el título de la ventana según se utilice para crear o modificar una reserva
        /// </summary>
        public string Title => Booking.Id != "" ? "Actualizar reserva" : "Crear reserva";
        
        
        /// <summary>
        /// Modo Singleton del ViewModel
        /// </summary>
        private static FormBookingViewModel? _instance;
        public static FormBookingViewModel Instance => _instance ??= new FormBookingViewModel();
        
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
        /// Constructor de la clase
        /// Crea una reserva vacía sobre la que crear
        /// Asigna los comandos de guardar y cancelar
        /// </summary>
        private FormBookingViewModel()
        {
            Booking = new BookingModel();
            SaveCommand = new RelayCommand(async _ => await Save());
            CancelCommand = new RelayCommand(async _ => await Cancel());
        }

        
        /// <summary>
        /// Función para cancelar una reserva
        /// Se referencia en el comando de cancelar
        /// </summary>
        private async Task Cancel()
        {
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
                f.RoomNumber = Booking.RoomNumber;
                var roomId = (await RoomService.GetRoomsFilteredAsync(f))?.Items.First().Id;
                Console.WriteLine(roomId);
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
    }
}