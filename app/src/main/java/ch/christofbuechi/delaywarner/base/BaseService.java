package ch.christofbuechi.delaywarner.base;

import java.io.IOException;
import java.lang.annotation.Annotation;

import ch.christofbuechi.delaywarner.network.APIError;
import ch.christofbuechi.delaywarner.network.APIException;
import ch.christofbuechi.delaywarner.network.SchedulerWrapper;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Christof on 07.01.2017.
 */
public class BaseService {
        private final Retrofit retrofit;
        private final SchedulerWrapper schedulerWrapper;

        public BaseService(Retrofit retrofit, SchedulerWrapper schedulerWrapper) {
            this.retrofit = retrofit;
            this.schedulerWrapper = schedulerWrapper;
        }

        //set to public because of this issue: https://github.com/orfjackal/retrolambda/issues/89
        public <T> Observable<T> applyHTTPErrorHandling(Response<T> response) {
            if (!response.isSuccessful()) {
                APIError apiError;
                Converter<ResponseBody, APIError> converter = retrofit.responseBodyConverter(APIError.class, new Annotation[0]);
                try {
                    apiError = converter.convert(response.errorBody());
                    return Observable.error(new APIException(response, apiError));
                } catch (IOException e) {
                    return Observable.error(new APIException(response));
                }
            } else {
                return Observable.just(response.body());
            }
        }

        //set to public because of this issue: https://github.com/orfjackal/retrolambda/issues/89
        public <T> Observable<T> applySchedulers(Observable<T> observable) {
            return observable.subscribeOn(schedulerWrapper.getIOScheduler()).observeOn(schedulerWrapper.getMainScheduler());
        }
}
