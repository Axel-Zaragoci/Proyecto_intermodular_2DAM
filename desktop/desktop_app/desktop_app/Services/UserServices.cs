using desktop_app.Models;
using desktop_app.ViewModels;
using desktop_app.Views;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace desktop_app.Services
{
    public class UsersService
    {
        public static class UserNavigationContext
        {
            public static UserUtilitiesMode Mode { get; set; }
            public static UserModel? User { get; set; }
        }

        private static readonly JsonSerializerOptions JsonOptions = new()
        {
            PropertyNameCaseInsensitive = true
        };
        private static StringContent JsonBody(object payload) => new StringContent(JsonSerializer.Serialize(payload, JsonOptions), Encoding.UTF8, "application/json");

        private static async Task<T?> ReadJsonAsync<T>(HttpResponseMessage resp, CancellationToken ct)
        {
            var json = await resp.Content.ReadAsStringAsync(ct);
            return JsonSerializer.Deserialize<T>(json, JsonOptions);
        }

        private static string Url(string relative) => $"{ApiService.BaseUrl}{relative}";
        public async Task<IReadOnlyList<UserModel>> GetAllAsync(CancellationToken ct = default)
        {
            using var resp = await ApiService._httpClient.GetAsync(Url("user"), ct);
            resp.EnsureSuccessStatusCode();

            var data = await ReadJsonAsync<List<UserModel>>(resp, ct);
            return data ?? new List<UserModel>();
        }

        public async Task DeleteByIdAsync(string id, CancellationToken ct = default)
        {
            using var resp = await ApiService._httpClient.DeleteAsync(Url($"user/delete/{id}"), ct);
            resp.EnsureSuccessStatusCode();
        }

        public async Task<UserModel?> CreateAsync(UserModel user, CancellationToken ct = default)
        {
            var payload = new
            {
                firstName = user.FirstName,
                lastName = user.LastName,
                email = user.Email,
                dni = user.Dni,
                phoneNumber = user.PhoneNumber,
                cityName = user.CityName,
                imageRoute = user.ImageRoute,
                rol = user.Rol,
                vipStatus = user.VipStatus,
                birthDate = user.BirthDate,
                gender = user.Gender
            };

            using var resp = await ApiService._httpClient.PostAsync(Url("user/register"), JsonBody(payload), ct);
            resp.EnsureSuccessStatusCode();

            return await ReadJsonAsync<UserModel>(resp, ct);
        }

        public async Task<UserModel?> UpdateAsync(UserModel user, CancellationToken ct = default)
        {
            var payload = new
            {
                id = user.Id,
                firstName = user.FirstName,
                lastName = user.LastName,
                email = user.Email,
                dni = user.Dni,
                phoneNumber = user.PhoneNumber,
                cityName = user.CityName,
                imageRoute = user.ImageRoute,
                rol = user.Rol,
                vipStatus = user.VipStatus,
                birthDate = user.BirthDate,
                gender = user.Gender
            };

            using var req = new HttpRequestMessage(HttpMethod.Put, Url("user/update"))
            {
                Content = JsonBody(payload)
            };

            using var resp = await ApiService._httpClient.SendAsync(req, ct);
            resp.EnsureSuccessStatusCode();

            return await ReadJsonAsync<UserModel>(resp, ct);
        }

        public async Task ReloadIntoAsync(
            ObservableCollection<UserModel> target,
            ICollectionView? viewToRefresh = null,
            CancellationToken ct = default)
        {
            var list = await GetAllAsync(ct);

            target.Clear();
            foreach (var u in list)
                target.Add(u);

            viewToRefresh?.Refresh();
        }

        public async Task DeleteAndRemoveAsync(
            UserModel user,
            ObservableCollection<UserModel> target,
            ICollectionView? viewToRefresh = null,
            CancellationToken ct = default)
        {
            await DeleteByIdAsync(user.Id, ct);

            target.Remove(user);
            viewToRefresh?.Refresh();
        }
        public async Task<UserModel?> SaveAsync(UserUtilitiesMode mode, UserModel user, CancellationToken ct = default)
        {
            return mode switch
            {
                UserUtilitiesMode.Create => await CreateAsync(user, ct),
                UserUtilitiesMode.Edit => await UpdateAsync(user, ct),
                _ => null
            };
        }
    }
}
