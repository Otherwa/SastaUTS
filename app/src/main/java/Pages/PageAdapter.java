package Pages;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Options.ORBookingFragment;
import Options.SavedTrains;
import Options.SettingsFragment;
import Options.TicketJourneyFragment;

public class PageAdapter extends FragmentStateAdapter {


    private static final int NUM_PAGES = 4;
    private String mDataForSettings;

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
            case 2:
                // Pass data to SettingsFragment
                return new SavedTrains();
            case 3:
                // Pass data to SettingsFragment
                return new SettingsFragment();
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
            case 2:
                return "Saved Trains";
            case 3:
                return "Settings"; // Tab title for SettingsFragment
            default:
                return null;
        }
    }
}
