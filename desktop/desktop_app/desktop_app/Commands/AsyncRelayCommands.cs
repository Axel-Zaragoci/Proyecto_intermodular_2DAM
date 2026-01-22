using System;
using System.Threading.Tasks;
using System.Windows.Input;

namespace desktop_app.Commands
{
    // ICommand para métodos async (llamadas a API)
    public class AsyncRelayCommand : ICommand
    {
        private readonly Func<Task> _executeAsync;
        private readonly Func<bool>? _canExecute;
        private bool _isRunning;

        public AsyncRelayCommand(Func<Task> executeAsync, Func<bool>? canExecute = null)
        {
            _executeAsync = executeAsync;
            _canExecute = canExecute;
        }

        public event EventHandler? CanExecuteChanged;

        public bool CanExecute(object? parameter)
        {
            // Si está corriendo, deshabilitamos el botón para evitar doble click
            if (_isRunning) return false;
            return _canExecute?.Invoke() ?? true;
        }

        public async void Execute(object? parameter)
        {
            _isRunning = true;
            RaiseCanExecuteChanged();

            try
            {
                await _executeAsync();
            }
            finally
            {
                _isRunning = false;
                RaiseCanExecuteChanged();
            }
        }

        public void RaiseCanExecuteChanged()
            => CanExecuteChanged?.Invoke(this, EventArgs.Empty);
    }
}
