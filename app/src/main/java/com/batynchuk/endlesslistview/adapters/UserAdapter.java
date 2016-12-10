package com.batynchuk.endlesslistview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.batynchuk.endlesslistview.MainActivity;
import com.batynchuk.endlesslistview.R;
import com.batynchuk.endlesslistview.models.User;
import com.batynchuk.endlesslistview.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context context) {
        mContext = context;
        mUsers = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return this.mUsers != null ? mUsers.size() : 0;
    }

    @Override
    public User getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.user_list_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        User user = getItem(position);
        holder.mTvUserItem.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));

        return view;
    }

    public void setUsers(List<User> users, int page) {
        if (users == null || users.size() == 0)
            return;
        if (this.mUsers.size() <= (Constants.PAGE_LIMIT * page)) {
            this.mUsers.addAll(users);
        } else {
            int from = Constants.PAGE_LIMIT * page;
            int to = Constants.PAGE_LIMIT * (page + 1);

            for (int i = from; i < to; i++) {
                this.mUsers.set(i, users.get(i % Constants.PAGE_LIMIT));
            }
        }
        notifyDataSetChanged();
    }

    public void cleanUp(int page) {
        int from = Constants.PAGE_LIMIT * page;
        int to = Constants.PAGE_LIMIT * (page + 1);

        for (int i = from; i < to; i++) {
            if (i < mUsers.size()) {
                mUsers.set(i, null);
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.tv_user_item)
        TextView mTvUserItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
