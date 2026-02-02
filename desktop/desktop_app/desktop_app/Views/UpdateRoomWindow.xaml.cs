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
            DataContext = new UpdateRoomViewModel(room);
        }
    }
}
