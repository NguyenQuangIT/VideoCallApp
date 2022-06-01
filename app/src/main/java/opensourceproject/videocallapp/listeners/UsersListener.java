package opensourceproject.videocallapp.listeners;

import opensourceproject.videocallapp.models.User;

public interface UsersListener {

    void initiateVideoMeeting(User user);

    void inititateAudioMeeting(User user);

    void onMultipleUsersAction(Boolean isMultipleUsersSelected);
}
