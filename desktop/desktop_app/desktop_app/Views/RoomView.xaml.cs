using System.Windows.Controls;
using desktop_app.ViewModels;

namespace desktop_app.Views
{
    public partial class RoomView : UserControl
    {
        public RoomView()
        {
            InitializeComponent();

            // Aquí solo conectamos View con ViewModel
            DataContext = new RoomViewModel();
        }
    }
}
