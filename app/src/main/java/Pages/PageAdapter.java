package Pages;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import BookingOptions.ORBookingFragment;
import BookingOptions.TicketJourneyFragment;

public class PageAdapter extends FragmentStateAdapter {

    private static final int NUM_PAGES = 2; // Assuming you have two pages

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the Fragment instance for each position
        switch (position) {
            case 0:
                return new ORBookingFragment();
            case 1:
                return new TicketJourneyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        // Return the number of pages in the ViewPager
        return NUM_PAGES;
    }

    public String getTabTitle(int position) {
        // Return the title for each tab based on position
        switch (position) {
            case 0:
                return "Booking"; // Tab title for ORBookingFragment
            case 1:
                return "Ticket Journey"; // Tab title for TicketJourneyFragment
            default:
                return null;
        }
    }
}
