using desktop_app.Commands;
using desktop_app.Services;
using System.Windows.Input;
using desktop_app.Views;

namespace desktop_app.ViewModels
{
    public class MainViewModel : ViewModelBase
    {
        public ICommand ShowUsersCommand { get; } = new RelayCommand(_ => NavigationService.Instance.NavigateTo<UserView>());
        public ICommand ShowBookingsCommand { get; } = new RelayCommand(_ => NavigationService.Instance.NavigateTo<BookingView>());
        public ICommand ShowRoomsCommand { get; } = new RelayCommand(_ => NavigationService.Instance.NavigateTo<RoomView>());
    }
}
