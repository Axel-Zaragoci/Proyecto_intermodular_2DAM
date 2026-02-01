using System.Windows;
using System.Windows.Controls;

namespace desktop_app.Views
{
    /// <summary>
    /// Lógica de interacción para BookingView.xaml
    /// </summary>
    public partial class BookingView : UserControl
    {
        public BookingView()
        {
            InitializeComponent();
        }
        
        /// <summary>
        /// Método que calcula el ancho de las columnas con datos de la tabla
        /// </summary>
        /// 
        /// <param name="sender">
        /// </param>
        /// <param name="e">
        /// </param>
        private void ListView_SizeChanged(object sender, SizeChangedEventArgs e)
        {
            if (sender is not ListView listView || listView.View is not GridView gridView)
                return;

            int fixedColumns = 3;
            double fixedWidth = gridView.Columns[0].Width + gridView.Columns[^1].Width + gridView.Columns[^2].Width;

            double availableWidth = listView.ActualWidth - fixedWidth - 10;
            double autoWidth = availableWidth / (gridView.Columns.Count - fixedColumns);

            for (int i = 1; i < gridView.Columns.Count - fixedColumns; i++)
            {
                gridView.Columns[i].Width = autoWidth;
            }
        }

    }
}
