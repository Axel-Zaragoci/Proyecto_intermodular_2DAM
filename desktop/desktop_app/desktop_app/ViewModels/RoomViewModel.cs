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
    public class RoomViewModel : ViewModelBase
    {
        private ObservableCollection<RoomModel> _rooms = new();
        public ObservableCollection<RoomModel> Rooms
        {
            get => _rooms;
            set => SetProperty(ref _rooms, value);
        }

        private string _statusText = "";
        public string StatusText
        {
            get => _statusText;
            set => SetProperty(ref _statusText, value);
        }

        private string _type = "";
        public string Type
        {
            get => _type;
            set => SetProperty(ref _type, value == "Todos" ? "" : value);
        }

        private bool _onlyAvailable;
        public bool OnlyAvailable
        {
            get => _onlyAvailable;
            set => SetProperty(ref _onlyAvailable, value);
        }

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

        public AsyncRelayCommand SearchCommand { get; }
        public AsyncRelayCommand RefreshCommand { get; }
        public RelayCommand ClearFiltersCommand { get; }
        public RelayCommand DeleteRoomCommand { get; }

        private RoomModel? _selectedRoom;
        public RoomModel? SelectedRoom
        {
            get => _selectedRoom;
            set => SetProperty(ref _selectedRoom, value);
        }

        private RoomsFilter _lastFilter = new RoomsFilter();

        public ICommand GoCreateRoomCommand { get; }
        public ICommand GoUpdateRoomCommand { get; }
        public ICommand BackToRoomsCommand { get; }

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

        private async Task DeleteRoomAsync(RoomModel? room)
        {
            if (room == null) return;

            var confirm = MessageBox.Show(
                $"¿Eliminar la habitación {room.RoomNumber}?",
                "Confirmar eliminación",
                MessageBoxButton.YesNo,
                MessageBoxImage.Warning);

            if (confirm != MessageBoxResult.Yes)
                return;

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
    }

    public class CreateRoomViewModel : ViewModelBase
    {
        private RoomModel _room = new() { IsAvailable = true, Rate = 0 };
        public RoomModel Room
        {
            get => _room;
            set => SetProperty(ref _room, value);
        }

        private string _extrasText = "";
        public string ExtrasText
        {
            get => _extrasText;
            set => SetProperty(ref _extrasText, value);
        }

        private string _extraImagesText = "";
        public string ExtraImagesText
        {
            get => _extraImagesText;
            set => SetProperty(ref _extraImagesText, value);
        }

        public AsyncRelayCommand SaveCommand { get; }
        public RelayCommand CancelCommand { get; }

        // Subida de fotos (paths locales)
        private string? _mainImageLocalPath;
        private List<string> _extraImagesLocalPaths = new();

        public RelayCommand PickMainImageLocalCommand { get; }
        public RelayCommand PickExtraImagesLocalCommand { get; }

        private string _mainImageLabel = "Sin seleccionar";
        public string MainImageLabel
        {
            get => _mainImageLabel;
            set => SetProperty(ref _mainImageLabel, value);
        }

        private string _extraImagesLabel = "0 seleccionadas";
        public string ExtraImagesLabel
        {
            get => _extraImagesLabel;
            set => SetProperty(ref _extraImagesLabel, value);
        }

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

        private async Task SaveAsync()
        {
            try
            {
                // Validaciones básicas
                if (string.IsNullOrWhiteSpace(Room.RoomNumber))
                    throw new Exception("RoomNumber es obligatorio.");

                if (string.IsNullOrWhiteSpace(Room.Type))
                    throw new Exception("Type es obligatorio.");

                if (Room.MaxGuests <= 0)
                    throw new Exception("MaxGuests debe ser mayor que 0.");

                // Extras (csv -> list)
                Room.Extras = (ExtrasText ?? "")
                    .Split(',')
                    .Select(x => x.Trim())
                    .Where(x => !string.IsNullOrWhiteSpace(x))
                    .ToList();

                // 1) Subir MainImage si se seleccionó
                if (!string.IsNullOrWhiteSpace(_mainImageLocalPath))
                {
                    var uploadedMain = await ImageService.UploadSingleAsync(_mainImageLocalPath);
                    if (uploadedMain == null)
                        throw new Exception("No se pudo subir la imagen principal.");

                    Room.MainImage = uploadedMain.Url; // "/uploads/xxx.jpg"
                }

                // 2) Subir ExtraImages si hay seleccionadas
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

                // 3) Crear habitación (usa el CreateRoomAsync que lanza excepción si falla)
                var created = await RoomService.CreateRoomAsync(Room);

                // 4) Navegar al final
                NavigationService.Instance.NavigateTo<RoomView>();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }

    public class UpdateRoomViewModel : ViewModelBase
    {
        private RoomModel _room;
        public class RoomImageItem
        {
            public string Url { get; set; } = "";          // "/uploads/xxx.jpg"
            public string AbsoluteUrl { get; set; } = "";  // "http://localhost:3000/uploads/xxx.jpg"
            public bool IsMain { get; set; }               // para badge Principal
        }

        public RoomModel Room
        {
            get => _room;
            set => SetProperty(ref _room, value);
        }

        private string _extrasText = "";
        public string ExtrasText
        {
            get => _extrasText;
            set => SetProperty(ref _extrasText, value);
        }

        private string _extraImagesText = "";
        public string ExtraImagesText
        {
            get => _extraImagesText;
            set => SetProperty(ref _extraImagesText, value);
        }

        public AsyncRelayCommand SaveCommand { get; }
        public RelayCommand CancelCommand { get; }

        // imágenes
        private string? _mainImageLocalPath;
        private List<string> _extraImagesLocalPaths = new();

        public RelayCommand PickMainImageLocalCommand { get; }
        public RelayCommand PickExtraImagesLocalCommand { get; }

        private string _mainImageLabel = "Sin seleccionar";
        public string MainImageLabel
        {
            get => _mainImageLabel;
            set => SetProperty(ref _mainImageLabel, value);
        }

        private string _extraImagesLabel = "0 seleccionadas";
        public string ExtraImagesLabel
        {
            get => _extraImagesLabel;
            set => SetProperty(ref _extraImagesLabel, value);
        }


        public ObservableCollection<RoomImageItem> ExistingImages { get; } = new();

        public ICommand DeleteImageCommand { get; }

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
        // Llamar cuando cargas la habitación
        public void RefreshExistingImages()
        {
            ExistingImages.Clear();

            // Principal
            if (!string.IsNullOrWhiteSpace(Room.MainImage))
            {
                ExistingImages.Add(new RoomImageItem
                {
                    Url = Room.MainImage,
                    AbsoluteUrl = ImageService.ToAbsoluteUrl(Room.MainImage),
                    IsMain = true
                });
            }

            // Extras
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

        private async Task DeleteImageAsync(object p)
        {
            if (p is not RoomImageItem img) return;

            var confirm = MessageBox.Show(
                "¿Seguro que quieres borrar esta imagen?\n\nSe eliminará del servidor y se quitará de la habitación.",
                "Confirmar borrado",
                MessageBoxButton.YesNo,
                MessageBoxImage.Warning);

            if (confirm != MessageBoxResult.Yes)
                return;

         
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

        private async Task SaveAsync()
        {
            try
            {
                // (Opcional) actualizar extras desde csv
                Room.Extras = (ExtrasText ?? "")
                    .Split(',')
                    .Select(x => x.Trim())
                    .Where(x => !string.IsNullOrWhiteSpace(x))
                    .ToList();

                // Main image
                if (!string.IsNullOrWhiteSpace(_mainImageLocalPath))
                {
                    var up = await ImageService.UploadSingleAsync(_mainImageLocalPath);
                    if (up == null) throw new Exception("No se pudo subir la imagen principal.");
                    Room.MainImage = up.Url;
                }

                // Extra images
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
