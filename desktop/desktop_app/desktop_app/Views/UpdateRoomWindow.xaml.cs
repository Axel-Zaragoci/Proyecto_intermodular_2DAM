using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

using desktop_app.Models;
using desktop_app.ViewModels;

namespace desktop_app.Views
{
    public partial class UpdateRoomWindow : Window
    {
        public UpdateRoomWindow(RoomModel room)
        {
            InitializeComponent();

            var vm = new UpdateRoomViewModel(room);
            vm.RequestClose += ok =>
            {
                DialogResult = ok;
                Close();
            };

            DataContext = vm;
        }

        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }
    }
}
