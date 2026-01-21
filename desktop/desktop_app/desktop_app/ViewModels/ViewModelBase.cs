using System.ComponentModel;

namespace desktop_app.ViewModels
{
    /// <summary>
    /// Clase base para los ViewModels que implementa la interfaz <see cref="INotifyPropertyChanged"/> para notificar cambios en las propiedades a la vista
    /// </summary>
    public class ViewModelBase : INotifyPropertyChanged
    {
        /// <summary>
        /// Evento que se dispara cuando cambia el valor de una propiedad
        /// </summary>
        public event PropertyChangedEventHandler? PropertyChanged;

        /// <summary>
        /// Notifica que una propiedad ha cambiado
        /// </summary>
        /// 
        /// <param name="propertyName">
        /// Nombre de la propiedad cuyo valor ha cambiado
        /// </param>
        protected virtual void OnPropertyChanged(string propertyName)
        {
            this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
