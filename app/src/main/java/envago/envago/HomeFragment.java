package envago.envago;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by vikas on 21-09-2016.
 */
public class HomeFragment extends Fragment {

    ListView categories;
    public int[] images = {R.drawable.air, R.drawable.earth, R.drawable.water, R.drawable.rockice, R.drawable.volunteer, R.drawable.all};
    ImageView map_button;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.homepage_activity, container, false);



        map_button = (ImageView)v.findViewById(R.id.map_button);

        categories = (ListView) v.findViewById(R.id.main_list);
        //  tabs = (ScrollingTabContainerView)findViewById(R.id.tabs);
        categories.setAdapter(new CategoriesAdapter(getContext(), images));

        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });



        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position==0 || position == 1 || position == 2 || position == 3)
                {
                    Intent intent = new Intent(getActivity(), Adventure_list.class);
                    intent.putExtra("main_id","1");
                    intent.putExtra("sub_id",String.valueOf(position+1));
                    intent.putExtra("status","single");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                else if (position == 4){

                }
                else {

                    Intent intent = new Intent(getActivity(), Adventure_list.class);
                    intent.putExtra("status","all");


                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }

            }
        });

        return v;

    }

  /*  //--------------------------------Category-API----------------------------

    public void get_list()
    {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Categoryyyy",s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1"))
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put(GlobalConstants.USERID,CommonUtils.UserID(getActivity()));
                params.put(GlobalConstants.CATEGORY_ID,GlobalConstants.CATEGORY_ID);

                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(cat_request);
    }*/
}
