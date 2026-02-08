using System;
using System.Data;
using System.Net.Http;
using System.Windows;
using System.Windows.Controls;
using desktop_app.Services;

namespace desktop_app.Views
{
    public partial class LoginView : Window
    {
        public LoginView()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Maneja el evento Click del botón de inicio de sesión.
        /// </summary>
        private async void Login_Click(object sender, RoutedEventArgs e)
        {
            LoginButton.IsEnabled = false;
            StatusText.Text = "";

            try
            {
                var email = EmailTextBox.Text?.Trim();
                var password = PasswordBox.Password;

                if (string.IsNullOrWhiteSpace(email) || string.IsNullOrWhiteSpace(password))
                {
                    StatusText.Text = "Email y Contraseña son obligatorios.";
                    return;
                }

                var rol = await AuthService.LoginAsync(email, password);

                if (rol == "Usuario")
                {
                    StatusText.Text = "No tienes permisos para acceder.";
                    return;
                }

                new MainWindow().Show();
                this.Close();
            }
            catch (HttpRequestException ex)
            {
                StatusText.Text = $"Error de red: {ex.Message}";
            }
            catch (Exception ex)
            {
                StatusText.Text = ex.Message;
            }
            finally
            {
                LoginButton.IsEnabled = true;
            }
        }
        /// <summary>
        /// Alterna la visibilidad de la contraseña entre modo oculto (<see cref="PasswordBox"/>) y modo visible (TextBox).
        /// </summary>
        private void TogglePassword_Click(object sender, RoutedEventArgs e)
        {
            if (PasswordBox.Visibility == Visibility.Visible)
            {
                PasswordTextBox.Text = PasswordBox.Password;
                PasswordBox.Visibility = Visibility.Collapsed;
                PasswordTextBox.Visibility = Visibility.Visible;
            }
            else
            {
                PasswordBox.Password = PasswordTextBox.Text;
                PasswordTextBox.Visibility = Visibility.Collapsed;
                PasswordBox.Visibility = Visibility.Visible;
            }
        }
    }
}
