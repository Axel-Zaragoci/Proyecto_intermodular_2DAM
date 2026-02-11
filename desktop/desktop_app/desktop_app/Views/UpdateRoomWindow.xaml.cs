using System.Windows.Controls;
using desktop_app.Models;
using desktop_app.ViewModels;

namespace desktop_app.Views
{
    public partial class UpdateRoomWindow : UserControl
    {
        public UpdateRoomWindow(RoomModel room)
        {
            InitializeComponent();
            DataContext = new desktop_app.ViewModels.Room.UpdateRoomViewModel(room);
        }

        private void UserControl_Loaded(object sender, System.Windows.RoutedEventArgs e)
        {
            // Forzar el foco al título para que el ScrollViewer empiece arriba
            HeaderMain.Focus();
        }
    }
}
