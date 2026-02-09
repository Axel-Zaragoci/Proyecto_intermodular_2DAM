using desktop_app.Commands;
using desktop_app.Models;
using desktop_app.Services;
using desktop_app.Views;
using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;

namespace desktop_app.ViewModels
{
    /// <summary>
    /// ViewModel para la vista de lista de habitaciones.
    /// </summary>
    public class RoomViewModel : ViewModelBase
    {
        #region Propiedades

        /// <summary>Colección de habitaciones mostradas.</summary>
        private ObservableCollection<RoomModel> _rooms = new();
        public ObservableCollection<RoomModel> Rooms
        {
            get => _rooms;
            set => SetProperty(ref _rooms, value);
        }

        /// <summary>Texto de estado (resultados, cargando, error).</summary>
        private string _statusText = "";
        public string StatusText
        {
            get => _statusText;
            set => SetProperty(ref _statusText, value);
        }

        /// <summary>Habitación seleccionada.</summary>
        private RoomModel? _selectedRoom;
        public RoomModel? SelectedRoom
        {
            get => _selectedRoom;
            set => SetProperty(ref _selectedRoom, value);
        }

        private RoomsFilter _lastFilter = new RoomsFilter();

        #endregion

        #region Propiedades de Filtro

        private string _type = "";
        /// <summary>Tipo de habitación para filtrar.</summary>
        public string Type
        {
            get => _type;
            set => SetProperty(ref _type, value == "Todos" ? "" : value);
        }

        private bool _onlyAvailable;
        /// <summary>Filtrar solo disponibles.</summary>
        public bool OnlyAvailable
        {
            get => _onlyAvailable;
            set => SetProperty(ref _onlyAvailable, value);
        }

        private string _guestsText = "";
        /// <summary>Número de huéspedes (texto).</summary>
        public string GuestsText
        {
            get => _guestsText;
            set => SetProperty(ref _guestsText, value);
        }

        private string _minPriceText = "";
        /// <summary>Precio mínimo (texto).</summary>
        public string MinPriceText
        {
            get => _minPriceText;
            set => SetProperty(ref _minPriceText, value);
        }

        private string _maxPriceText = "";
        /// <summary>Precio máximo (texto).</summary>
        public string MaxPriceText
        {
            get => _maxPriceText;
            set => SetProperty(ref _maxPriceText, value);
        }

        private bool _hasExtraBed;
        /// <summary>Filtrar con cama extra.</summary>
        public bool HasExtraBed
        {
            get => _hasExtraBed;
            set => SetProperty(ref _hasExtraBed, value);
        }

        private bool _hasCrib;
        /// <summary>Filtrar con cuna.</summary>
        public bool HasCrib
        {
            get => _hasCrib;
            set => SetProperty(ref _hasCrib, value);
        }

        private bool _hasOffer;
        /// <summary>Filtrar con oferta.</summary>
        public bool HasOffer
        {
            get => _hasOffer;
            set => SetProperty(ref _hasOffer, value);
        }

        private string _extrasText = "";
        /// <summary>Extras a buscar (CSV).</summary>
        public string ExtrasText
        {
            get => _extrasText;
            set => SetProperty(ref _extrasText, value);
        }

        private string _sortBy = "roomNumber";
        /// <summary>Campo de ordenamiento.</summary>
        public string SortBy
        {
            get => _sortBy;
            set => SetProperty(ref _sortBy, value);
        }

        private string _sortOrder = "asc";
        /// <summary>Dirección de ordenamiento.</summary>
        public string SortOrder
        {
            get => _sortOrder;
            set => SetProperty(ref _sortOrder, value);
        }

        #endregion

        #region Commands

        /// <summary>Comando para buscar habitaciones.</summary>
        public AsyncRelayCommand SearchCommand { get; }

        /// <summary>Comando para refrescar la lista.</summary>
        public AsyncRelayCommand RefreshCommand { get; }

        /// <summary>Comando para limpiar filtros.</summary>
        public RelayCommand ClearFiltersCommand { get; }

        /// <summary>Comando para eliminar una habitación.</summary>
        public RelayCommand DeleteRoomCommand { get; }

        /// <summary>Comando para ir a crear habitación.</summary>
        public ICommand GoCreateRoomCommand { get; }

        /// <summary>Comando para ir a editar habitación.</summary>
        public ICommand GoUpdateRoomCommand { get; }

        /// <summary>Comando para volver a la lista.</summary>
        public ICommand BackToRoomsCommand { get; }

        #endregion

        /// <summary>
        /// Inicializa el ViewModel y carga datos iniciales.
        /// </summary>
        public RoomViewModel()
        {
            SearchCommand = new AsyncRelayCommand(SearchAsync);
            RefreshCommand = new AsyncRelayCommand(RefreshAsync);
            DeleteRoomCommand = new RelayCommand(async param => await DeleteRoomAsync(param as RoomModel));
            ClearFiltersCommand = new RelayCommand(_ => ClearFilters());

            GoCreateRoomCommand = new RelayCommand(_ =>
                NavigationService.Instance.NavigateTo<CreateRoomWindow>());

            GoUpdateRoomCommand = new RelayCommand(param =>
            {
                if (param is not RoomModel room) return;
                NavigationService.Instance.NavigateTo(() => new UpdateRoomWindow(room));
            });

            BackToRoomsCommand = new RelayCommand(_ =>
                NavigationService.Instance.NavigateTo<RoomView>());

            _ = LoadInitialAsync();
        }

        #region Métodos Privados

        /// <summary>
        /// Elimina una habitación tras confirmación.
        /// </summary>
        /// <param name="room">Habitación a eliminar.</param>
        private async Task DeleteRoomAsync(RoomModel? room)
        {
            if (room == null) return;

            var confirm = MessageBox.Show(
                $"¿Eliminar la habitación {room.RoomNumber}?",
                "Confirmar eliminación",
                MessageBoxButton.YesNo,
                MessageBoxImage.Warning);

            if (confirm != MessageBoxResult.Yes) return;

            var ok = await RoomService.DeleteRoomAsync(room.Id);

            if (!ok)
            {
                MessageBox.Show("No se pudo eliminar la habitación (API).");
                return;
            }

            Rooms.Remove(room);
            StatusText = $"Resultados: {Rooms.Count}";
        }

        private async Task LoadInitialAsync()
        {
            _lastFilter = new RoomsFilter();
            await LoadRoomsAsync(_lastFilter);
        }

        private async Task SearchAsync()
        {
            try
            {
                var filter = BuildFilterFromUi();
                _lastFilter = filter;
                await LoadRoomsAsync(filter);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private async Task RefreshAsync()
        {
            await LoadRoomsAsync(_lastFilter);
        }

        private void ClearFilters()
        {
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
            _lastFilter = new RoomsFilter();
        }

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

            Rooms = new ObservableCollection<RoomModel>(response.Items);
            StatusText = $"Resultados: {Rooms.Count}";
        }

        /// <summary>
        /// Construye el filtro desde los valores de la UI.
        /// </summary>
        /// <returns>Filtro configurado.</returns>
        /// <exception cref="Exception">Si algún campo tiene formato inválido.</exception>
        private RoomsFilter BuildFilterFromUi()
        {
            var f = new RoomsFilter();

            if (!string.IsNullOrWhiteSpace(Type))
                f.Type = Type;

            if (OnlyAvailable)
                f.IsAvailable = true;

            if (!string.IsNullOrWhiteSpace(GuestsText))
            {
                if (int.TryParse(GuestsText, out var g) && g > 0)
                    f.Guests = g;
                else
                    throw new Exception("Guests debe ser un entero válido.");
            }

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

            if (HasExtraBed) f.HasExtraBed = true;
            if (HasCrib) f.HasCrib = true;
            if (HasOffer) f.HasOffer = true;

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

            if (!string.IsNullOrWhiteSpace(SortBy))
                f.SortBy = SortBy;

            if (!string.IsNullOrWhiteSpace(SortOrder))
                f.SortOrder = SortOrder;

            return f;
        }

        #endregion
    }

    /// <summary>
    /// ViewModel para crear una habitación.
    /// </summary>
    public class CreateRoomViewModel : ViewModelBase
    {
        /// <summary>Modelo de la habitación en creación.</summary>
        private RoomModel _room = new() { IsAvailable = true, Rate = 0 };
        public RoomModel Room
        {
            get => _room;
            set => SetProperty(ref _room, value);
        }

        private string _extrasText = "";
        /// <summary>Extras como texto CSV.</summary>
        public string ExtrasText
        {
            get => _extrasText;
            set => SetProperty(ref _extrasText, value);
        }

        private string _extraImagesText = "";
        /// <summary>Info de imágenes extra.</summary>
        public string ExtraImagesText
        {
            get => _extraImagesText;
            set => SetProperty(ref _extraImagesText, value);
        }

        private string? _mainImageLocalPath;
        private List<string> _extraImagesLocalPaths = new();

        private string _mainImageLabel = "Sin seleccionar";
        /// <summary>Nombre del archivo de imagen principal.</summary>
        public string MainImageLabel
        {
            get => _mainImageLabel;
            set => SetProperty(ref _mainImageLabel, value);
        }

        private string _extraImagesLabel = "0 seleccionadas";
        /// <summary>Contador de imágenes extra.</summary>
        public string ExtraImagesLabel
        {
            get => _extraImagesLabel;
            set => SetProperty(ref _extraImagesLabel, value);
        }

        /// <summary>Comando para guardar.</summary>
        public AsyncRelayCommand SaveCommand { get; }

        /// <summary>Comando para cancelar.</summary>
        public RelayCommand CancelCommand { get; }

        /// <summary>Comando para seleccionar imagen principal.</summary>
        public RelayCommand PickMainImageLocalCommand { get; }

        /// <summary>Comando para seleccionar imágenes extra.</summary>
        public RelayCommand PickExtraImagesLocalCommand { get; }

        public CreateRoomViewModel()
        {
            SaveCommand = new AsyncRelayCommand(SaveAsync);
            CancelCommand = new RelayCommand(_ => NavigationService.Instance.NavigateTo<RoomView>());
            PickMainImageLocalCommand = new RelayCommand(_ => PickMainImageLocal());
            PickExtraImagesLocalCommand = new RelayCommand(_ => PickExtraImagesLocal());
        }

        private void PickMainImageLocal()
        {
            var dlg = new OpenFileDialog
            {
                Filter = "Imágenes|*.jpg;*.jpeg;*.png;*.webp;*.gif",
                Multiselect = false
            };

            if (dlg.ShowDialog() != true) return;

            _mainImageLocalPath = dlg.FileName;
            MainImageLabel = Path.GetFileName(_mainImageLocalPath);
        }

        private void PickExtraImagesLocal()
        {
            var dlg = new OpenFileDialog
            {
                Filter = "Imágenes|*.jpg;*.jpeg;*.png;*.webp;*.gif",
                Multiselect = true
            };

            if (dlg.ShowDialog() != true) return;

            _extraImagesLocalPaths = dlg.FileNames.ToList();
            ExtraImagesLabel = $"{_extraImagesLocalPaths.Count} seleccionadas";
            ExtraImagesText = string.Join(", ", _extraImagesLocalPaths.Select(Path.GetFileName));
        }

        /// <summary>
        /// Valida, sube imágenes y crea la habitación.
        /// </summary>
        /// <exception cref="Exception">Si hay errores de validación o subida.</exception>
        private async Task SaveAsync()
        {
            try
            {
                if (string.IsNullOrWhiteSpace(Room.RoomNumber))
                    throw new Exception("RoomNumber es obligatorio.");

                if (string.IsNullOrWhiteSpace(Room.Type))
                    throw new Exception("Type es obligatorio.");

                if (Room.MaxGuests <= 0)
                    throw new Exception("MaxGuests debe ser mayor que 0.");

                Room.Extras = (ExtrasText ?? "")
                    .Split(',')
                    .Select(x => x.Trim())
                    .Where(x => !string.IsNullOrWhiteSpace(x))
                    .ToList();

                if (!string.IsNullOrWhiteSpace(_mainImageLocalPath))
                {
                    var uploadedMain = await ImageService.UploadSingleAsync(_mainImageLocalPath);
                    if (uploadedMain == null)
                        throw new Exception("No se pudo subir la imagen principal.");
                    Room.MainImage = uploadedMain.Url;
                }

                if (_extraImagesLocalPaths != null && _extraImagesLocalPaths.Count > 0)
                {
                    var uploadedExtras = await ImageService.UploadManyAsync(_extraImagesLocalPaths);
                    if (uploadedExtras == null)
                        throw new Exception("No se pudieron subir las imágenes extra.");
                    Room.ExtraImages = uploadedExtras.Select(x => x.Url).ToList();
                    ExtraImagesText = string.Join(", ", Room.ExtraImages);
                    ExtraImagesLabel = $"{Room.ExtraImages.Count} seleccionadas";
                }
                else
                {
                    Room.ExtraImages = new List<string>();
                    ExtraImagesText = "";
                    ExtraImagesLabel = "0 seleccionadas";
                }

                var created = await RoomService.CreateRoomAsync(Room);
                NavigationService.Instance.NavigateTo<RoomView>();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }

    /// <summary>
    /// ViewModel para editar una habitación.
    /// </summary>
    public class UpdateRoomViewModel : ViewModelBase
    {
        /// <summary>Item de imagen para la galería.</summary>
        public class RoomImageItem
        {
            /// <summary>Ruta relativa.</summary>
            public string Url { get; set; } = "";

            /// <summary>URL absoluta para mostrar.</summary>
            public string AbsoluteUrl { get; set; } = "";

            /// <summary>Es imagen principal.</summary>
            public bool IsMain { get; set; }
        }

        private RoomModel _room;
        /// <summary>Habitación en edición.</summary>
        public RoomModel Room
        {
            get => _room;
            set => SetProperty(ref _room, value);
        }

        private string _extrasText = "";
        /// <summary>Extras como CSV.</summary>
        public string ExtrasText
        {
            get => _extrasText;
            set => SetProperty(ref _extrasText, value);
        }

        private string _extraImagesText = "";
        /// <summary>Info de imágenes extra.</summary>
        public string ExtraImagesText
        {
            get => _extraImagesText;
            set => SetProperty(ref _extraImagesText, value);
        }

        /// <summary>Imágenes existentes para la galería.</summary>
        public ObservableCollection<RoomImageItem> ExistingImages { get; } = new();

        private string? _mainImageLocalPath;
        private List<string> _extraImagesLocalPaths = new();

        private string _mainImageLabel = "Sin seleccionar";
        /// <summary>Nombre del archivo de imagen principal.</summary>
        public string MainImageLabel
        {
            get => _mainImageLabel;
            set => SetProperty(ref _mainImageLabel, value);
        }

        private string _extraImagesLabel = "0 seleccionadas";
        /// <summary>Contador de imágenes extra nuevas.</summary>
        public string ExtraImagesLabel
        {
            get => _extraImagesLabel;
            set => SetProperty(ref _extraImagesLabel, value);
        }

        /// <summary>Comando para guardar cambios.</summary>
        public AsyncRelayCommand SaveCommand { get; }

        /// <summary>Comando para cancelar.</summary>
        public RelayCommand CancelCommand { get; }

        /// <summary>Comando para seleccionar nueva imagen principal.</summary>
        public RelayCommand PickMainImageLocalCommand { get; }

        /// <summary>Comando para seleccionar nuevas imágenes extra.</summary>
        public RelayCommand PickExtraImagesLocalCommand { get; }

        /// <summary>Comando para eliminar una imagen.</summary>
        public ICommand DeleteImageCommand { get; }

        /// <summary>
        /// Inicializa el ViewModel con una habitación existente.
        /// </summary>
        /// <param name="room">Habitación a editar.</param>
        public UpdateRoomViewModel(RoomModel room)
        {
            _room = room;

            ExtrasText = string.Join(", ", room.Extras ?? new());
            ExtraImagesText = string.Join(", ", room.ExtraImages ?? new());

            SaveCommand = new AsyncRelayCommand(SaveAsync);
            CancelCommand = new RelayCommand(_ => NavigationService.Instance.NavigateTo<RoomView>());
            PickMainImageLocalCommand = new RelayCommand(_ => PickMainImageLocal());
            PickExtraImagesLocalCommand = new RelayCommand(_ => PickExtraImagesLocal());

            DeleteImageCommand = new RelayCommand(
                async (p) => await DeleteImageAsync(p),
                (p) => p is RoomImageItem it && !string.IsNullOrWhiteSpace(it.Url)
            );

            RefreshExistingImages();
        }

        /// <summary>
        /// Recarga la galería de imágenes existentes.
        /// </summary>
        public void RefreshExistingImages()
        {
            ExistingImages.Clear();

            if (!string.IsNullOrWhiteSpace(Room.MainImage))
            {
                ExistingImages.Add(new RoomImageItem
                {
                    Url = Room.MainImage,
                    AbsoluteUrl = ImageService.ToAbsoluteUrl(Room.MainImage),
                    IsMain = true
                });
            }

            if (Room.ExtraImages != null)
            {
                foreach (var u in Room.ExtraImages.Where(x => !string.IsNullOrWhiteSpace(x)))
                {
                    ExistingImages.Add(new RoomImageItem
                    {
                        Url = u,
                        AbsoluteUrl = ImageService.ToAbsoluteUrl(u),
                        IsMain = false
                    });
                }
            }

            OnPropertyChanged(nameof(ExistingImages));
        }

        /// <summary>
        /// Elimina una imagen del servidor y del modelo.
        /// </summary>
        /// <param name="p">RoomImageItem a eliminar.</param>
        private async Task DeleteImageAsync(object p)
        {
            if (p is not RoomImageItem img) return;

            var confirm = MessageBox.Show(
                "¿Seguro que quieres borrar esta imagen?\n\nSe eliminará del servidor.",
                "Confirmar borrado",
                MessageBoxButton.YesNo,
                MessageBoxImage.Warning);

            if (confirm != MessageBoxResult.Yes) return;

            var ok = await ImageService.DeleteImageAsync(img.Url);
            if (!ok)
            {
                MessageBox.Show("No se pudo borrar la imagen en el servidor.", "Error",
                    MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            ExistingImages.Remove(img);

            if (img.IsMain)
            {
                Room.MainImage = null;
                MainImageLabel = "Sin seleccionar";
            }
            else
            {
                Room.ExtraImages.Remove(img.Url);
                ExtraImagesLabel = $"{Room.ExtraImages.Count} seleccionadas";
                ExtraImagesText = string.Join(", ", Room.ExtraImages);
            }

            OnPropertyChanged(nameof(Room));
        }

        private void PickMainImageLocal()
        {
            var dlg = new OpenFileDialog
            {
                Filter = "Imágenes|*.jpg;*.jpeg;*.png;*.webp;*.gif",
                Multiselect = false
            };

            if (dlg.ShowDialog() != true) return;

            _mainImageLocalPath = dlg.FileName;
            MainImageLabel = Path.GetFileName(_mainImageLocalPath);
        }

        private void PickExtraImagesLocal()
        {
            var dlg = new OpenFileDialog
            {
                Filter = "Imágenes|*.jpg;*.jpeg;*.png;*.webp;*.gif",
                Multiselect = true
            };

            if (dlg.ShowDialog() != true) return;

            _extraImagesLocalPaths = dlg.FileNames.ToList();
            ExtraImagesLabel = $"{_extraImagesLocalPaths.Count} seleccionadas";
        }

        /// <summary>
        /// Guarda los cambios en la habitación.
        /// </summary>
        /// <exception cref="Exception">Si hay errores de subida o actualización.</exception>
        private async Task SaveAsync()
        {
            try
            {
                Room.Extras = (ExtrasText ?? "")
                    .Split(',')
                    .Select(x => x.Trim())
                    .Where(x => !string.IsNullOrWhiteSpace(x))
                    .ToList();

                if (!string.IsNullOrWhiteSpace(_mainImageLocalPath))
                {
                    var up = await ImageService.UploadSingleAsync(_mainImageLocalPath);
                    if (up == null) throw new Exception("No se pudo subir la imagen principal.");
                    Room.MainImage = up.Url;
                }

                if (_extraImagesLocalPaths.Count > 0)
                {
                    var ups = await ImageService.UploadManyAsync(_extraImagesLocalPaths);
                    if (ups == null) throw new Exception("No se pudieron subir las imágenes extra.");

                    Room.ExtraImages ??= new List<string>();
                    Room.ExtraImages.AddRange(ups.Select(x => x.Url));
                    ExtraImagesText = string.Join(", ", Room.ExtraImages);
                }

                var ok = await RoomService.UpdateRoomAsync(Room.Id, Room);
                if (!ok) throw new Exception("No se pudo actualizar la habitación (API).");

                NavigationService.Instance.NavigateTo<RoomView>();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }
}
