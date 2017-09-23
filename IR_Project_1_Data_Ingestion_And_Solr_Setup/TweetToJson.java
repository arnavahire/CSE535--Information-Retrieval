
public class TweetToJson {
	String topic;
	String screen_name;
	String tweet_date;
	String tweet_lang;
	String text_en;
	String text_ru;
	String text_tr;
	String text_es;
	String text_ko;
	boolean Retweet;
	String tweet_text;
	String tweet_urls;
	String hashtags;
	String De;
	String mentions;
	String tweet_loc;
	String tweet_emoticons;
	
	
	public void setRetweet(boolean bool)
	{
		this.Retweet=bool;
	}
	
	public void setemoticon(String str)
	{	
		this.tweet_emoticons=str;
	}
	public void setTopic(String str)
	{
		this.topic=str;
	}
	public void setScreenName(String str)
	{
		this.screen_name=str;
	}
	public void setLang(String str)
	{
		this.tweet_lang=str;
	}
	public void setCreated_at(String str)
	{
		this.tweet_date=str;
	}
	public void setText_en(String str)
	{
		this.text_en=str;
	}
	public void setText_ru(String str)
	{
		this.text_ru=str;
	}
	public void setText_es(String str)
	{
		this.text_es=str;
	}
	public void setText_tr(String str)
	{
		this.text_tr=str;
	}
	public void setText_ko(String str)
	{
		this.text_ko=str;
	}
	public void setText_de(String str)
	{
		this.De=str;
	}
	public void setMentions(String str)
	{
		this.mentions=str;
	}
	public void setTweet_loc(String str)
	{
		this.tweet_loc=str;
	}
	public void setTweet_text(String str)
	{
		this.tweet_text=str;
	}
	public void appendtwitter_url(String str)
	{
		this.tweet_urls=str;
	}
	public void appendTwitter_hashtag(String str)
	{
		this.hashtags=str;
	}
	
}
