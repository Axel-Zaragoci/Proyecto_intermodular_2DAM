using desktop_app.Commands;
using desktop_app.Models;
using desktop_app.Services;
using System;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;

namespace desktop_app.ViewModels
{
    public class RoomViewModel : ViewModelBase
    {
        // =========================
        // 1) DATOS PARA LA VISTA
        // =========================

        // Esto es lo que el DataGrid va a mostrar.
        // ObservableCollection notifica al UI cuando agregas/quitas items.
        private ObservableCollection<RoomModel> _rooms = new();
        public ObservableCollection<RoomModel> Rooms
        {
            get => _rooms;
            set => SetProperty(ref _rooms, value);
        }

        // Texto para mostrar estado (opcional): "Cargando...", "0 resultados", etc.
        private string _statusText = "";
        public string StatusText
        {
            get => _statusText;
            set => SetProperty(ref _statusText, value);
        }

        // =========================
        // 2) PROPIEDADES DE FILTRO (bind con controles)
        // =========================

        private string _type = ""; // "" significa "cualquiera"
        public string Type
        {
            get => _type;
            set => SetProperty(ref _type, value);
        }

        private bool _onlyAvailable;
        public bool OnlyAvailable
        {
            get => _onlyAvailable;
            set => SetProperty(ref _onlyAvailable, value);
        }

        // Para inputs de UI, muchas veces es más fácil guardar como string
        // y luego parsear, así no petas si el usuario escribe letras.
        private string _guestsText = "";
        public string GuestsText
        {
            get => _guestsText;
            set => SetProperty(ref _guestsText, value);
        }

        private string _minPriceText = "";
        public string MinPriceText
        {
            get => _minPriceText;
            set => SetProperty(ref _minPriceText, value);
        }

        private string _maxPriceText = "";
        public string MaxPriceText
        {
            get => _maxPriceText;
            set => SetProperty(ref _maxPriceText, value);
        }

        private bool _hasExtraBed;
        public bool HasExtraBed
        {
            get => _hasExtraBed;
            set => SetProperty(ref _hasExtraBed, value);
        }

        private bool _hasCrib;
        public bool HasCrib
        {
            get => _hasCrib;
            set => SetProperty(ref _hasCrib, value);
        }

        private bool _hasOffer;
        public bool HasOffer
        {
            get => _hasOffer;
            set => SetProperty(ref _hasOffer, value);
        }

        private string _extrasText = "";
        public string ExtrasText
        {
            get => _extrasText;
            set => SetProperty(ref _extrasText, value);
        }

        private string _sortBy = "roomNumber";
        public string SortBy
        {
            get => _sortBy;
            set => SetProperty(ref _sortBy, value);
        }

        private string _sortOrder = "asc";
        public string SortOrder
        {
            get => _sortOrder;
            set => SetProperty(ref _sortOrder, value);
        }

        // =========================
        // 3) COMMANDS (botones)
        // =========================

        public AsyncRelayCommand SearchCommand { get; }
        public AsyncRelayCommand RefreshCommand { get; }
        public RelayCommand ClearFiltersCommand { get; }

        // Guardamos el último filtro real usado para "Refrescar"
        private RoomsFilter _lastFilter = new RoomsFilter();

        // =========================
        // 4) CONSTRUCTOR
        // =========================

        public RoomViewModel()
        {
            // Conectamos botones con métodos
            SearchCommand = new AsyncRelayCommand(SearchAsync);
            RefreshCommand = new AsyncRelayCommand(RefreshAsync);
            ClearFiltersCommand = new RelayCommand(
                _ => ClearFilters(),
                _ => true // aquí tu condición
            );
            ClearFiltersCommand = new RelayCommand(_ => ClearFilters());


            // Carga inicial (equivalente a Loaded en code-behind)
            _ = LoadInitialAsync();
        }

        private async Task LoadInitialAsync()
        {
            // Empezamos sin filtros
            _lastFilter = new RoomsFilter();
            await LoadRoomsAsync(_lastFilter);
        }

        // =========================
        // 5) LÓGICA PRINCIPAL
        // =========================

        private async Task SearchAsync()
        {
            // 1) Construimos un RoomsFilter real desde propiedades del VM
            var filter = BuildFilterFromUi();

            // 2) Guardamos como último filtro para refrescar
            _lastFilter = filter;

            // 3) Cargamos desde API
            await LoadRoomsAsync(filter);
        }

        private async Task RefreshAsync()
        {
            // Refresca usando el último filtro aplicado
            await LoadRoomsAsync(_lastFilter);
        }

        private void ClearFilters()
        {
            // Reseteamos propiedades (el UI se actualiza solo)
            Type = "";
            OnlyAvailable = false;
            GuestsText = "";
            MinPriceText = "";
            MaxPriceText = "";
            HasExtraBed = false;
            HasCrib = false;
            HasOffer = false;
            ExtrasText = "";
            SortBy = "roomNumber";
            SortOrder = "asc";

            // Y el último filtro vuelve a ser vacío
            _lastFilter = new RoomsFilter();
        }

        // Carga desde API y actualiza el DataGrid
        private async Task LoadRoomsAsync(RoomsFilter filter)
        {
            StatusText = "Cargando habitaciones...";

            var response = await RoomService.GetRoomsFilteredAsync(filter);

            if (response == null)
            {
                StatusText = "Error conectando con la API.";
                MessageBox.Show("No se pudo conectar con la API.");
                return;
            }

            // Convertimos la lista en ObservableCollection (o actualizamos Rooms)
            Rooms = new ObservableCollection<RoomModel>(response.Items);

            StatusText = $"Resultados: {Rooms.Count}";
        }

        // Construye el filtro EXACTO que tu backend entiende
        private RoomsFilter BuildFilterFromUi()
        {
            var f = new RoomsFilter();

            // type (si está vacío => no filtramos)
            if (!string.IsNullOrWhiteSpace(Type))
                f.Type = Type;

            // available
            if (OnlyAvailable)
                f.IsAvailable = true;

            // guests
            if (!string.IsNullOrWhiteSpace(GuestsText))
            {
                if (int.TryParse(GuestsText, out var g) && g > 0)
                    f.Guests = g;
                else
                    throw new Exception("Guests debe ser un entero válido.");
            }

            // prices (aceptamos coma o punto según cultura)
            if (!string.IsNullOrWhiteSpace(MinPriceText))
            {
                if (decimal.TryParse(MinPriceText, NumberStyles.Number, CultureInfo.CurrentCulture, out var min))
                    f.MinPrice = min;
                else
                    throw new Exception("MinPrice no es válido.");
            }

            if (!string.IsNullOrWhiteSpace(MaxPriceText))
            {
                if (decimal.TryParse(MaxPriceText, NumberStyles.Number, CultureInfo.CurrentCulture, out var max))
                    f.MaxPrice = max;
                else
                    throw new Exception("MaxPrice no es válido.");
            }

            // flags: si están marcadas, filtramos true
            if (HasExtraBed) f.HasExtraBed = true;
            if (HasCrib) f.HasCrib = true;
            if (HasOffer) f.HasOffer = true;

            // extras: "wifi,parking"
            if (!string.IsNullOrWhiteSpace(ExtrasText))
            {
                var extras = ExtrasText
                    .Split(',')
                    .Select(x => x.Trim())
                    .Where(x => !string.IsNullOrWhiteSpace(x))
                    .ToList();

                if (extras.Count > 0)
                    f.Extras = extras;
            }

            // sort
            if (!string.IsNullOrWhiteSpace(SortBy))
                f.SortBy = SortBy;

            if (!string.IsNullOrWhiteSpace(SortOrder))
                f.SortOrder = SortOrder;

            return f;
        }
    }
}
