using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using desktop_app.Commands;
using desktop_app.Models;
using System.Windows.Input;
using desktop_app.Services;

namespace desktop_app.ViewModels
{
    public enum UserUtilitiesMode { Create, View, Edit }

    public class UserUtilitiesViewModel : ViewModelBase
    {
        private readonly UsersService _usersService = new();

        public UserUtilitiesMode Mode { get; }
        public bool IsReadOnly => Mode == UserUtilitiesMode.View;

        public UserModel User { get; }

        public ICommand SaveCommand { get; }
        public ICommand CancelCommand { get; }

        public UserUtilitiesViewModel(UserUtilitiesMode mode, UserModel? user = null)
        {
            Mode = mode;

            User = user ?? new UserModel
            {
                Rol = "Usuario",
                BirthDate = DateTime.Today.AddYears(-16),
                VipStatus = false
            };

            SaveCommand = new AsyncRelayCommand(SaveAsync, () => Mode != UserUtilitiesMode.View);
            CancelCommand = new RelayCommand<object>(_ => Cancel());
        }

        private async Task SaveAsync()
        {
            await _usersService.SaveAsync(Mode, User);

            NavigationService.Instance.NavigateTo<desktop_app.Views.UserView>();
        }

        private void Cancel()
        {
            NavigationService.Instance.NavigateTo<desktop_app.Views.UserView>();
        }
    }
}
