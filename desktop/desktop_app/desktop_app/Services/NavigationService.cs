using System.ComponentModel;
using System.Windows.Controls;
using desktop_app.Views;

namespace desktop_app.Services
{
    public class NavigationService : INotifyPropertyChanged
    {
        private static NavigationService? _instance;
        public static NavigationService Instance => _instance ??= new NavigationService();

        private List<UserControl> _stackViews = [new UserView()];
        public UserControl CurrentView
        {
            get => _stackViews[0];
            protected set
            {
                if (_stackViews[0] == value) return;
                StackViews.Insert(0, value);
            }
        }

        public List<UserControl> StackViews
        {
            get => _stackViews;
        }

        public void NavigateTo<T> () where T : UserControl, new()
        {
            CurrentView = StackViews.Find(e => e is T) ?? new T();
            OnPropertyChanged(nameof(CurrentView));
        }

        public event PropertyChangedEventHandler? PropertyChanged;
        protected void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
