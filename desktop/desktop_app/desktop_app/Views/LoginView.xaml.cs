using System;
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

        private async void Login_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            LoginButton.IsEnabled = false;
            StatusText.Text = "";

            try
            {
                var email = EmailTextBox.Text?.Trim();
                var password = PasswordBox.Password;

                if (string.IsNullOrWhiteSpace(email) || string.IsNullOrWhiteSpace(password))
                {
                    StatusText.Text = "Email y Password son obligatorios.";
                    return;
                }

                await AuthService.LoginAsync(email, password);

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
    }
}
