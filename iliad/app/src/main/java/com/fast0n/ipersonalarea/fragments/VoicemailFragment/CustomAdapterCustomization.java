package com.fast0n.ipersonalarea.fragments.VoicemailFragment;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.ipersonalarea.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CustomAdapterCustomization extends RecyclerView.Adapter<CustomAdapterCustomization.MyViewHolder> {

    Context context;
    String token;
    private List<DataCustomizationFragments> optionsList;

    CustomAdapterCustomization(Context context, List<DataCustomizationFragments> optionsList, String token) {
        this.context = context;
        this.optionsList = optionsList;
        this.token = token;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DataCustomizationFragments c = optionsList.get(position);
        holder.textView.setText(c.textView);


        if (c.toggle.equals("false"))
            holder.toggle.setChecked(false);
        else
            holder.toggle.setChecked(true);

        holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                final String site_url = context.getString(R.string.site_url);

                if (!isChecked) {
                    String url = site_url + "?changevoicemailoptions=true&update=" + c.name + "&token=" + token + "&activate=0";
                    request_options_services(url, holder.textView.getText() + " " + String.valueOf(isChecked).replace("false", "disattivato"));
                } else if (isChecked) {

                    String url = site_url + "?changevoicemailoptions=true&update=" + c.name + "&token=" + token + "&activate=1";
                    request_options_services(url, holder.textView.getText() + " " + String.valueOf(isChecked).replace("true", "attivo"));
                }
            }

            private void request_options_services(String url, String labelOn) {

                RequestQueue queue = Volley.newRequestQueue(context);

                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject json_raw = new JSONObject(response.toString());
                                    String iliad = json_raw.getString("iliad");

                                    JSONObject json = new JSONObject(iliad);
                                    String string_response = json.getString("0");

                                    if (string_response.equals("true")) {

                                        Toasty.warning(context, labelOn, Toast.LENGTH_SHORT, true).show();
                                    }

                                } catch (JSONException ignored) {
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                queue.add(getRequest);

            }

        });


    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_options_services, parent, false);
        return new MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        Switch toggle;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView1);
            toggle = view.findViewById(R.id.toggle);

        }
    }
}