namespace desktop_app.Events;

public static class BookingEvents
{
    public static event Func<Task>? OnBookingChanged;

    public static async Task RaiseBookingChanged()
    {
        if (OnBookingChanged != null) await OnBookingChanged.Invoke();
    }
}
